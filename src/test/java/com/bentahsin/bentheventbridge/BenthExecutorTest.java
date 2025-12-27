package com.bentahsin.bentheventbridge;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class BenthExecutorTest {

    @Test
    void testExecuteInvokesMethod() throws Throwable {
        TestListener listener = new TestListener();

        Method method = TestListener.class.getMethod("onEvent", DummyEvent.class);
        MethodHandle handle = MethodHandles.lookup().unreflect(method).bindTo(listener);

        BenthExecutor executor = new BenthExecutor(handle, DummyEvent.class, "");

        DummyEvent event = new DummyEvent();
        executor.execute(listener, event);

        assertTrue(listener.executed, "Standart event çalıştırılamadı!");
    }

    @Test
    void testChannelFilterPass() throws Throwable {
        ChannelListener listener = new ChannelListener();

        Method method = ChannelListener.class.getMethod("onMessage", BenthMessageEvent.class);
        MethodHandle handle = MethodHandles.lookup().unreflect(method).bindTo(listener);

        BenthExecutor executor = new BenthExecutor(handle, BenthMessageEvent.class, "savas-basladi");

        BenthMessageEvent event = new BenthMessageEvent(mock(Plugin.class), "savas-basladi", "veri");
        executor.execute(listener, event);

        assertTrue(listener.executed, "Kanal ismi doğru olduğu halde metot çalışmadı!");
    }

    @Test
    void testChannelFilterBlock() throws Throwable {
        ChannelListener listener = new ChannelListener();

        Method method = ChannelListener.class.getMethod("onMessage", BenthMessageEvent.class);
        MethodHandle handle = MethodHandles.lookup().unreflect(method).bindTo(listener);

        BenthExecutor executor = new BenthExecutor(handle, BenthMessageEvent.class, "savas-basladi");

        BenthMessageEvent event = new BenthMessageEvent(mock(Plugin.class), "ekonomi-guncelle", "veri");
        executor.execute(listener, event);

        assertFalse(listener.executed, "Kanal ismi YANLIŞ olduğu halde metot çalıştı! Filtre bozuk.");
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

    public static class ChannelListener implements Listener {
        boolean executed = false;
        public void onMessage(BenthMessageEvent ignored) {
            this.executed = true;
        }
    }
}