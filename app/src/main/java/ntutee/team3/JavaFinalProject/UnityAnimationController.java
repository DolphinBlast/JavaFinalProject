package ntutee.team3.JavaFinalProject;

import com.unity3d.player.UnityPlayer;

public class UnityAnimationController {


    public static Boolean AlarmRinning = false;


    public static void SetRandomAnimation()
    {
        new Thread(() -> {
            String[] animation = {"greet", "negative", "positive", "shock", "think"};
            while (!AlarmRinning) {
                try {
                    int sec = 1000 * (int) (Math.random() * 5 + 10);
                    Thread.sleep(sec);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int index = (int) (Math.random() * 5);
                if(!AlarmRinning)
                    UnityPlayer.UnitySendMessage("Spine4604", "PLayAnimation", animation[index]);
            }

        }).start();
    }

    public static void ReplayAnimation()
    {
        new Thread(() -> {
            while (AlarmRinning) {
                try {
                    int sec = 1000;
                    Thread.sleep(sec);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                UnityPlayer.UnitySendMessage("Spine4604", "PLayAnimation", "damage");
            }
            UnityPlayer.UnitySendMessage("Spine4604", "PLayAnimation", "extra_2");
            SetRandomAnimation();
        }).start();

    }

}
