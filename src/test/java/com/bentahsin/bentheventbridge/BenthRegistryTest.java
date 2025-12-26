package com.bentahsin.bentheventbridge;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class BenthRegistryTest {

    private Plugin mockPlugin;
    private PluginManager mockPluginManager;
    private BenthRegistry registry;
    private MockedStatic<Bukkit> mockedBukkit;

    @BeforeEach
    void setUp() {
        mockPlugin = mock(Plugin.class);
        mockPluginManager = mock(PluginManager.class);

        mockedBukkit = Mockito.mockStatic(Bukkit.class);
        mockedBukkit.when(Bukkit::getPluginManager).thenReturn(mockPluginManager);

        registry = new BenthRegistry(mockPlugin);
    }

    @AfterEach
    void tearDown() {
        mockedBukkit.close();
    }

    @Test
    void testRegisterSuccess() {
        ValidListener listener = new ValidListener();

        registry.register(listener);

        verify(mockPluginManager).registerEvent(
                eq(TestEvent.class),
                eq(listener),
                any(),
                any(EventExecutor.class),
                eq(mockPlugin),
                any(Boolean.class)
        );
    }

    @Test
    void testFailStaticMethod() {
        StaticMethodListener listener = new StaticMethodListener();
        assertThrows(IllegalStateException.class, () -> registry.register(listener));
    }

    @Test
    void testFailWrongParameterCount() {
        WrongParamCountListener listener = new WrongParamCountListener();
        assertThrows(IllegalArgumentException.class, () -> registry.register(listener));
    }

    @Test
    void testFailWrongParameterType() {
        WrongParamTypeListener listener = new WrongParamTypeListener();
        assertThrows(IllegalArgumentException.class, () -> registry.register(listener));
    }

    public static class TestEvent extends Event {
        private static final HandlerList handlers = new HandlerList();
        @Override public HandlerList getHandlers() { return handlers; }
        public static HandlerList getHandlerList() { return handlers; }
    }

    public static class ValidListener implements org.bukkit.event.Listener {
        @Subscribe
        public void onTest(TestEvent e) {}
    }

    public static class StaticMethodListener implements org.bukkit.event.Listener {
        @Subscribe
        public static void onTest(TestEvent e) {}
    }

    public static class WrongParamCountListener implements org.bukkit.event.Listener {
        @Subscribe
        public void onTest(TestEvent e, String ekstra) {}
    }

    public static class WrongParamTypeListener implements org.bukkit.event.Listener {
        @Subscribe
        public void onTest(String degilEvent) {}
    }
}