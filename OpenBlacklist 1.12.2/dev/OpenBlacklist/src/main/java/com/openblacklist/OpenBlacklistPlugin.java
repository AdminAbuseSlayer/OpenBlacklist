package com.openblacklist;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("OpenBlacklist")
@IFMLLoadingPlugin.SortingIndex(1001)
public class OpenBlacklistPlugin implements IFMLLoadingPlugin {
    @Override public String[] getASMTransformerClass() { return new String[]{"com.openblacklist.BlacklistTransformer"}; }
    @Override public String getModContainerClass() { return null; }
    @Override public String getSetupClass() { return null; }
    @Override public void injectData(Map<String, Object> data) { }
    @Override public String getAccessTransformerClass() { return null; }
}
