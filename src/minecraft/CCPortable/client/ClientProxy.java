package CCPortable.client;

import CCPortable.common.CommonProxy;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderThings()
    {
        MinecraftForgeClient.preloadTexture( "/CCPortable/Textures.png" );
    }
}