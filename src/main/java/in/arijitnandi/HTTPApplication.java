package in.arijitnandi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.quarkus.resteasy.common.runtime.QuarkusInjectorFactory;
import io.quarkus.resteasy.common.runtime.jackson.QuarkusJacksonSerializer;
import io.quarkus.resteasy.runtime.standalone.BufferAllocator;
import io.quarkus.resteasy.runtime.standalone.VertxRequestHandler;
import io.quarkus.resteasy.server.common.runtime.QuarkusResteasyDeployment;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import jakarta.enterprise.inject.spi.CDI;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class HTTPApplication implements QuarkusApplication {

    private static final int PORT = 8083;

    private static final Executor POOL_EXECUTOR = new ThreadPoolExecutor(
            4,
            16,
            60L,
            TimeUnit.SECONDS,
            new SynchronousQueue<>()
    );

    private final List<Class<?>> resources;

    private final List<Class<?>> providers;


    protected HTTPApplication() {
        this.resources = List.of(ExampleController.class);
        this.providers = List.of();
    }

    @Override
    public int run(String... args) throws Exception {
        CDI<Object> arcContainer = CDI.current();

        // Create a new Resteasy deployment
        QuarkusResteasyDeployment deployment = new QuarkusResteasyDeployment();

        deployment.setInjectorFactory(new QuarkusInjectorFactory());

        deployment.start();

        deployment.setRegisterBuiltin(true);

        // Register the MyResource class as a REST resource
        for (Class<?> resource: resources) {
            deployment.getRegistry().addPerRequestResource(resource);
        }

        QuarkusJacksonSerializer jacksonProvider = new QuarkusJacksonSerializer();
        deployment.getProviders().add(jacksonProvider);

        for (Class<?> provider: providers) {
            deployment.getProviderFactory().registerProvider(provider);
        }

        // Create a new Vert.x instance
        Vertx vertx = Vertx.vertx();

        Router router = Router.router(vertx);
        // Create a new HTTP server

        router.route().handler(new VertxRequestHandler(
                vertx,
                deployment,
                "/",
                new ResteasyVertxAllocator(256),
                POOL_EXECUTOR,
                5000L
        ));

        AtomicReference<Throwable> failureCause = new AtomicReference<>(null);

        HttpServer httpServer = vertx.createHttpServer()
                .requestHandler(router)
                .listen(PORT, "localhost", http -> {
                    if (http.succeeded()) {
                        System.out.println("HTTP server started on port " + PORT);
                        // Create a new HTTP client
                    } else {
                        failureCause.set(http.cause());
                        System.out.println("HTTP server failed to start");
                    }
                });

        if (failureCause.get() != null) {
            failureCause.get().printStackTrace();
            return 0;
        }

        deployment.stop();
        CountDownLatch latch = new CountDownLatch(1);

        httpServer.close((voidAsyncResult) -> {
            System.out.println("HTTP server closed.");
            latch.countDown();
        });

        latch.await();

        return 0;
    }

    private static class ResteasyVertxAllocator implements BufferAllocator {

        private final static boolean USE_DIRECT = true;

        private final int bufferSize;

        private ResteasyVertxAllocator(int bufferSize) {
            this.bufferSize = bufferSize;
        }

        @Override
        public ByteBuf allocateBuffer() {
            return allocateBuffer(USE_DIRECT);
        }

        @Override
        public ByteBuf allocateBuffer(boolean direct) {
            return allocateBuffer(direct, bufferSize);
        }

        @Override
        public ByteBuf allocateBuffer(int bufferSize) {
            return allocateBuffer(USE_DIRECT, bufferSize);
        }

        @Override
        public ByteBuf allocateBuffer(boolean direct, int bufferSize) {
            if (direct) {
                return PooledByteBufAllocator.DEFAULT.directBuffer(bufferSize);
            } else {
                return PooledByteBufAllocator.DEFAULT.heapBuffer(bufferSize);
            }
        }

        @Override
        public int getBufferSize() {
            return bufferSize;
        }
    }
}
