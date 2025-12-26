package com.bentahsin.bentheventbridge;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class BenthBusTest {

    private Plugin mockPlugin;
    private PluginManager mockPluginManager;
    private MockedStatic<Bukkit> mockedBukkit;

    @BeforeEach
    void setUp() {
        mockPlugin = mock(Plugin.class);
        mockPluginManager = mock(PluginManager.class);
        mockedBukkit = Mockito.mockStatic(Bukkit.class);
        mockedBukkit.when(Bukkit::getPluginManager).thenReturn(mockPluginManager);
    }

    @AfterEach
    void tearDown() {
        mockedBukkit.close();
    }

    @Test
    void testPublish() {
        String channel = "test-channel";
        String payload = "test-data";

        BenthBus.publish(mockPlugin, channel, payload);

        ArgumentCaptor<BenthMessageEvent> eventCaptor = ArgumentCaptor.forClass(BenthMessageEvent.class);
        verify(mockPluginManager).callEvent(eventCaptor.capture());

        BenthMessageEvent capturedEvent = eventCaptor.getValue();

        assertEquals(channel, capturedEvent.getChannel());
        assertEquals(payload, capturedEvent.getPayload());
        assertEquals(mockPlugin, capturedEvent.getSender());
    }
}