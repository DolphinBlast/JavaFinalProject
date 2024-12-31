package ntutee.team3.JavaFinalProject;

import com.unity3d.player.UnityPlayer;

public class UnityAnimationController {


    public static Boolean AlarmRinging = false;

    //不定時撥放動畫
    public static void SetRandomAnimationLoop()
    {
        new Thread(() -> {
            String[] animation = {"greet", "negative", "positive", "think", "damage"};
            while (!AlarmRinging) {
                try {
                    int sec = 1000 * ((int) (Math.random() * 5) + 10);
                    Thread.sleep(sec);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int index = (int) (Math.random() * 5);
                if(!AlarmRinging)
                    UnityPlayer.UnitySendMessage("Spine4604", "PLayAnimation", animation[index]);
            }

        }).start();

    }

    //撥放響鈴動畫以及鬧鐘關閉動畫
    public static void AlarmAnimation()
    {
        new Thread(() -> {
            while (AlarmRinging) {
                try {
                    int sec = 3000;
                    Thread.sleep(sec);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                UnityPlayer.UnitySendMessage("Spine4604", "PLayAnimation", "shock");
            }
            UnityPlayer.UnitySendMessage("Spine4604", "PLayAnimation", "extra_2");
            SetRandomAnimationLoop();
        }).start();
    }

}
