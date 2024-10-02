package cn.stars.starx.music.thread;

import cn.stars.starx.StarX;
import cn.stars.starx.music.api.MusicAPI;
import cn.stars.starx.music.api.user.QRCode;
import cn.stars.starx.music.api.user.QRCodeState;
import cn.stars.starx.music.api.user.ScanResult;
import cn.stars.starx.util.math.TimerUtil;
import lombok.Getter;

import java.io.IOException;

/**
 * @author ChengFeng
 * @since 2024/8/13
 **/
@Getter
public class LoginThread extends Thread {
    private QRCode qrCode;
    private ScanResult result;
    private final TimerUtil timer;

    public LoginThread() {
        timer = new TimerUtil();
        setName("Music-Login");
    }

    @Override
    public void run() {
        // 尝试5次获取二维码
        int count = 0;
        while (qrCode == null) {
            if (count >= 5) {
                StarX.showMsg("Please check your Internet connection.");
                return;
            }
            try {
                qrCode = MusicAPI.genQRCode();
            } catch (IOException e) {
                StarX.showMsg("Failed to generate QR Code. Retry...");
                count++;
            }
        }

        // 每隔一秒向服务器请求扫描结果
        do {
            if (timer.hasTimeElapsed(1000)) {
                result = MusicAPI.getScanResult(qrCode.getKey());
                StarX.showMsg("Login state: " + result.getState());
                timer.reset();
            }
        } while (result.getState() == QRCodeState.WAITING_SCAN || result.getState() == QRCodeState.WAITING_CONFIRM);
    }
}
