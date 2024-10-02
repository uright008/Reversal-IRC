package cn.stars.starx.music.api.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

/**
 * @author ChengFeng
 * @since 2024/8/11
 **/
@Getter
@Setter
@AllArgsConstructor
public class QRCode {
    BufferedImage image;
    String key;
}
