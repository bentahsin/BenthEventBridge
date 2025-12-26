package com.bentahsin.bentheventbridge;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BenthExecutorTest {

    @Test
    void testExecuteInvokesMethod() throws Throwable {
        TestListener listener = new TestListener();

        Method method = TestListener.class.getMethod("onEvent", DummyEvent.class);
        MethodHandle handle = MethodHandles.lookup().unreflect(method).bindTo(listener);

        BenthExecutor executor = new BenthExecutor(handle, DummyEvent.class);

        DummyEvent event = new DummyEvent();
        executor.execute(listener, event);

        assertTrue(listener.executed, "Metot çalıştırılamadı!");
    }

    public static class DummyEvent extends Event {
        private static final HandlerList handlers = new HandlerList();
        @Override public HandlerList getHandlers() { return handlers; }
    }

    public static class TestListener implements Listener {
        boolean executed = false;

        public void onEvent(DummyEvent ignored) {
            this.executed = true;
        }
    }
}