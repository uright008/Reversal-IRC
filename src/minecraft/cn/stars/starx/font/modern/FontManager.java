package cn.stars.starx.font.modern;

import net.minecraft.client.Minecraft;

import java.util.HashMap;

public class FontManager {

    // FOR ANYONE WHO VISITS THIS CLASS: CREATE A HASHMAP FOR EACH FONT AND BASICALLY COPY THE GIVEN METHOD

    private static final HashMap<Integer, ModernFontRenderer> REGULAR = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> INTERNATIONAL = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> MONTSERRAT_MAP = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> ROBOTO_MAP = new HashMap<>();

    private static final HashMap<Integer, ModernFontRenderer> LIGHT_MAP = new HashMap<>();

    private static final HashMap<Integer, ModernFontRenderer> NUNITO = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> NUNITO_BOLD = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> MUSEO_SANS = new HashMap<>();

    private static final HashMap<Integer, ModernFontRenderer> NUNITO_LIGHT_MAP = new HashMap<>();

    private static final HashMap<Integer, ModernFontRenderer> POPPINS_BOLD = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> POPPINS_SEMI_BOLD = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> POPPINS_MEDIUM = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> POPPINS_REGULAR = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> POPPINS_LIGHT = new HashMap<>();

    private static final HashMap<Integer, ModernFontRenderer> QUICKSAND_MAP_MEDIUM = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> QUICKSAND_MAP_LIGHT = new HashMap<>();

    private static final HashMap<Integer, ModernFontRenderer> TAHOMA_BOLD = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> TAHOMA = new HashMap<>();

    private static final HashMap<Integer, ModernFontRenderer> MOREICONS = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> ICONS_2 = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> ICONS_3 = new HashMap<>();

    private static final HashMap<Integer, ModernFontRenderer> DREAMSCAPE = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> DREAMSCAPE_NO_AA = new HashMap<>();

    private static final HashMap<Integer, ModernFontRenderer> SOMATIC = new HashMap<>();

    private static final HashMap<Integer, ModernFontRenderer> BIKO = new HashMap<>();

    private static final HashMap<Integer, ModernFontRenderer> MONTSERRAT_HAIRLINE = new HashMap<>();

    private static final HashMap<Integer, ModernFontRenderer> PRODUCT_SANS_BOLD = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> PRODUCT_SANS_REGULAR = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> PRODUCT_SANS_MEDIUM = new HashMap<>();
    private static final HashMap<Integer, ModernFontRenderer> PRODUCT_SANS_LIGHT = new HashMap<>();

    private static final HashMap<Integer, ModernFontRenderer> SF_UI_PRO = new HashMap<>();

    private static final HashMap<Integer, ModernFontRenderer> HACK = new HashMap<>();

    // COPY THIS METHOD FOR EACH METHOD AND REPLACE FONTNAME WITH THE USED FONT FILE NAME
    public static MFont getMontserratMedium(final int size) {
        return get(MONTSERRAT_MAP, size, "Montserrat-Medium", true, true);
    }

    public static MFont getMontserratHairline(final int size) {
        return get(MONTSERRAT_HAIRLINE, size, "Montserrat-Hairline", true, true);
    }

    public static MFont getInternational(int size) {
        return get(INTERNATIONAL, size, "NotoSans-Regular", true, true, false, true);
    }

    public static MFont getRegular(int size) {
        return get(REGULAR,  size, "regular", true, true, false, true);
    }

    public static MFont getRobotoLight(final int size) {
        return get(ROBOTO_MAP, size, "Roboto-Light", true, true);
    }

    public static MFont getLight(final int size) {
        return get(LIGHT_MAP, size, "Light", true, true);
    }

    public static MFont getSFUIPro(final int size) {
        return get(SF_UI_PRO, size, "SF-UI-Pro", true, true);
    }

    public static MFont getPoppinsBold(final int size) {
        return get(POPPINS_BOLD, size, "Poppins-Bold", true, true);
    }

    public static MFont getPoppinsSemiBold(final int size) {
        return get(POPPINS_SEMI_BOLD, size, "Poppins-SemiBold", true, true);
    }

    public static MFont getPoppinsMedium(final int size) {
        return get(POPPINS_MEDIUM, size, "Poppins-Medium", true, true);
    }

    public static MFont getPoppinsRegular(final int size) {
        return get(POPPINS_REGULAR, size, "Poppins-Regular", true, true);
    }

    public static MFont getPoppinsLight(final int size) {
        return get(POPPINS_LIGHT, size, "Poppins-Light", true, true);
    }

    public static MFont getNunito(final int size) {
        return get(PRODUCT_SANS_REGULAR, size, "product_sans_regular", true, true, false, true);
    }

    public static MFont getNunitoBold(final int size) {
        return get(PRODUCT_SANS_BOLD, size, "product_sans_bold", true, true, false, true);
    }

    public static MFont getMuseo(final int size) {
        return get(MUSEO_SANS, size, "MuseoSans_900", true, true);
    }

    public static MFont getNunitoLight(final int size) {
        return get(PRODUCT_SANS_LIGHT, size, "product_sans_light", true, true);
    }

    public static MFont getQuicksandMedium(final int size) {
        return get(QUICKSAND_MAP_MEDIUM, size, "Quicksand-Medium", true, true);
    }

    public static MFont getQuicksandLight(final int size) {
        return get(QUICKSAND_MAP_LIGHT, size, "Quicksand-Light", true, true);
    }

    public static MFont getTahomaBold(final int size) {
        return get(TAHOMA_BOLD, size, "TahomaBold", true, true);
    }

    public static MFont getTahoma(final int size) {
        return get(TAHOMA, size, "Tahoma", true, true);
    }

    public static MFont getDreamscape(final int size) {
        return get(DREAMSCAPE, size, "Dreamscape", true, true);
    }

    public static MFont getSomatic(final int size) {
        return get(SOMATIC, size, "Somatic-Rounded", true, true);
    }

    public static MFont getDreamscapeNoAA(final int size) {
        return get(DREAMSCAPE_NO_AA, size, "Dreamscape", true, false);
    }

    public static MFont getMi(final int size) {
        return get(MOREICONS, size, "Moreicon", true, true);
    }

    public static MFont getIconsThree(final int size) {
        return get(ICONS_3, size, "Icon-3", true, true);
    }

    public static MFont getIconsTwo(final int size) {
        return get(ICONS_2, size, "Icon-2", true, true);
    }

    public static MFont getBiko(final int size) {
        return get(BIKO, size, "Biko_Regular", true, true);
    }

    public static MFont getPSB(final int size) {
        return get(PRODUCT_SANS_BOLD, size, "ProductSansBold", true, true, false, false);
    }

    public static MFont getPSR(final int size) {
        return get(PRODUCT_SANS_REGULAR, size, "product_sans_regular", true, true, false, false);
    }

    public static MFont getPSM(final int size) {
        return get(PRODUCT_SANS_MEDIUM, size, "ProductSansMedium", true, true, false, false);
    }

    public static MFont getProductSansLight(final int size) {
        return get(PRODUCT_SANS_LIGHT, size, "product_sans_light", true, true, false, true);
    }

    public static MFont getHack(final int size) {
        return get(HACK, size, "Hack-Regular", true, true);
    }

    public static net.minecraft.client.gui.FontRenderer getMinecraft() {
        return Minecraft.getMinecraft().fontRendererObj;
    }

    private static MFont get(HashMap<Integer, ModernFontRenderer> map, int size, String name, boolean fractionalMetrics, boolean AA) {
        return get(map, size, name, fractionalMetrics, AA, false, false);
    }

    private static MFont get(HashMap<Integer, ModernFontRenderer> map, int size, String name, boolean fractionalMetrics, boolean AA, boolean otf, boolean international) {
        if (!map.containsKey(size)) {
            final java.awt.Font font = FontUtil.getResource("starx/font/" + name + (otf ? ".otf" : ".ttf"), size);

            if (font != null) {
                map.put(size, new ModernFontRenderer(font, fractionalMetrics, AA, international));
            }
        }

        return map.get(size);
    }
}