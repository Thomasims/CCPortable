package CCPortable.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Random;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.opengl.GL11;

public class FixedWidthFontRenderer
{
    private int charWidth[];
    public int fontTextureName;
    public int FONT_HEIGHT;
    public int FONT_WIDTH;
    private int fontDisplayLists;
    private IntBuffer buffer;
    public Random fontRandom;

    public FixedWidthFontRenderer(GameSettings gamesettings, String s, RenderEngine renderengine)
    {
        FONT_HEIGHT = 9;
        FONT_WIDTH = 6;
        charWidth = new int[256];
        fontTextureName = 0;
        buffer = GLAllocation.createDirectIntBuffer(1024);
        fontRandom = new Random();
        BufferedImage bufferedimage;

        try
        {
            bufferedimage = ImageIO.read((RenderEngine.class).getResourceAsStream(s));
        }
        catch (IOException ioexception)
        {
            throw new RuntimeException(ioexception);
        }

        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int ai[] = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, ai, 0, i);

        for (int k = 0; k < 256; k++)
        {
            int l = k % 16;
            int k1 = k / 16;
            int j2 = 7;

            do
            {
                if (j2 < 0)
                {
                    break;
                }

                int i3 = l * 8 + j2;
                boolean flag = true;

                for (int i4 = 0; i4 < 8 && flag; i4++)
                {
                    int j4 = (k1 * 8 + i4) * i;
                    int l4 = ai[i3 + j4] & 0xff;

                    if (l4 > 0)
                    {
                        flag = false;
                    }
                }

                if (!flag)
                {
                    break;
                }

                j2--;
            }
            while (true);

            if (k == 32)
            {
                j2 = 2;
            }

            charWidth[k] = j2 + 2;
        }

        fontTextureName = renderengine.allocateAndSetupTexture(bufferedimage);
        fontDisplayLists = GLAllocation.generateDisplayLists(288);
        Tessellator tessellator = Tessellator.instance;

        for (int i1 = 0; i1 < 256; i1++)
        {
            int l1 = (FONT_WIDTH - charWidth[i1]) / 2;
            GL11.glNewList(fontDisplayLists + i1, GL11.GL_COMPILE);
            GL11.glTranslatef(l1, 0.0F, 0.0F);
            tessellator.startDrawingQuads();
            int k2 = (i1 % 16) * 8;
            int j3 = (i1 / 16) * 8;
            float f = 7.99F;
            float f1 = 0.0F;
            float f2 = 0.0F;
            tessellator.addVertexWithUV(0.0D, 0.0F + f, 0.0D, (float)k2 / 128F + f1, ((float)j3 + f) / 128F + f2);
            tessellator.addVertexWithUV(0.0F + f, 0.0F + f, 0.0D, ((float)k2 + f) / 128F + f1, ((float)j3 + f) / 128F + f2);
            tessellator.addVertexWithUV(0.0F + f, 0.0D, 0.0D, ((float)k2 + f) / 128F + f1, (float)j3 / 128F + f2);
            tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, (float)k2 / 128F + f1, (float)j3 / 128F + f2);
            tessellator.draw();
            GL11.glTranslatef(FONT_WIDTH - l1, 0.0F, 0.0F);
            GL11.glEndList();
        }

        for (int j1 = 0; j1 < 32; j1++)
        {
            int i2 = (j1 >> 3 & 1) * 85;
            int l2 = (j1 >> 2 & 1) * 170 + i2;
            int k3 = (j1 >> 1 & 1) * 170 + i2;
            int l3 = (j1 >> 0 & 1) * 170 + i2;

            if (j1 == 6)
            {
                l2 += 85;
            }

            boolean flag1 = j1 >= 16;

            if (gamesettings.anaglyph)
            {
                int k4 = (l2 * 30 + k3 * 59 + l3 * 11) / 100;
                int i5 = (l2 * 30 + k3 * 70) / 100;
                int j5 = (l2 * 30 + l3 * 70) / 100;
                l2 = k4;
                k3 = i5;
                l3 = j5;
            }

            if (flag1)
            {
                l2 /= 4;
                k3 /= 4;
                l3 /= 4;
            }

            GL11.glNewList(fontDisplayLists + 256 + j1, GL11.GL_COMPILE);
            GL11.glColor3f((float)l2 / 255F, (float)k3 / 255F, (float)l3 / 255F);
            GL11.glEndList();
        }
    }

    public void drawString(String s, int i, int j, int k)
    {
        renderString(s, i, j, k);
    }

    public void renderString(String s, int i, int j, int k)
    {
        if (s == null)
        {
            return;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTextureName);
        float f = (float)(k >> 16 & 0xff) / 255F;
        float f1 = (float)(k >> 8 & 0xff) / 255F;
        float f2 = (float)(k & 0xff) / 255F;
        float f3 = (float)(k >> 24 & 0xff) / 255F;

        if (f3 == 0.0F)
        {
            f3 = 1.0F;
        }

        GL11.glColor4f(f, f1, f2, f3);
        buffer.clear();
        GL11.glPushMatrix();
        GL11.glTranslatef(i, j, 0.0F);

        for (int l = 0; l < s.length(); l++)
        {
            if (l < s.length())
            {
                int i1 = ChatAllowedCharacters.allowedCharacters.indexOf(s.charAt(l));

                if (i1 < 0)
                {
                    i1 = ChatAllowedCharacters.allowedCharacters.indexOf('?');
                }

                if (i1 >= 0)
                {
                    buffer.put(fontDisplayLists + i1 + 32);
                }
            }

            if (buffer.remaining() == 0)
            {
                buffer.flip();
                GL11.glCallLists(buffer);
                buffer.clear();
            }
        }

        buffer.flip();
        GL11.glCallLists(buffer);
        GL11.glPopMatrix();
    }

    public int getStringWidth(String s)
    {
        if (s == null)
        {
            return 0;
        }

        int i = 0;

        for (int j = 0; j < s.length(); j++)
        {
            if (s.charAt(j) == '\247')
            {
                j++;
            }
            else
            {
                i += FONT_WIDTH;
            }
        }

        return i;
    }
}
