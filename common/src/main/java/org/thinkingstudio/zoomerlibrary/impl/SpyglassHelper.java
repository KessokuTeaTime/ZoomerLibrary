package org.thinkingstudio.zoomerlibrary.impl;

import org.thinkingstudio.zoomerlibrary.ZoomerLibrary;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

/**
 * An utility class whose sole purpose is to hold the spyglass tag
 */
public class SpyglassHelper {
    /**
     * The spyglass tag, which is used internally in order to unhardcode behavior specific to vanilla spyglasses
     */
    public static final TagKey<Item> SPYGLASSES = TagKey.of(RegistryKeys.ITEM, new Identifier(ZoomerLibrary.MODID, "spyglasses"));
}
