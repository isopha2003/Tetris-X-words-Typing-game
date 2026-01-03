import java.io.File; // 파일 클래스

import javax.sound.sampled.AudioInputStream; // 오디오 입력 스트림
import javax.sound.sampled.AudioSystem; // 오디오 시스템
import javax.sound.sampled.Clip; // 오디오 클립
import javax.sound.sampled.FloatControl; // 볼륨 컨트롤

class EffectSound { // 효과음 재생 클래스
    public static void play(String filePath, float b) { // 파일 경로와 볼륨을 받아 재생
        try {
            File soundFile = new File(filePath); // 파일 객체 생성
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile); // 오디오 스트림 생성

            Clip clip = AudioSystem.getClip(); // 클립 생성
            clip.open(audioStream); // 오디오 스트림 열기
            FloatControl gainControl = null; // 볼륨 컨트롤 초기화
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) { // 볼륨 조절 가능 확인
                gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); // 컨트롤 가져오기
                gainControl.setValue(b); // dB 값으로 볼륨 조절
            }
            clip.start(); // 클립 재생 (한번만)

        } catch (Exception e) { // 예외 처리
            e.printStackTrace(); // 스택 트레이스 출력
            System.out.println("효과음 재생 실패: " + filePath); // 오류 메시지
        }
    }
}

public class MusicPlayer { // 배경음악 재생 클래스
    private Clip clip; // 오디오 클립 객체

    public void play(String filePath, boolean loop) { // 파일 경로와 반복 여부
        try {
            File audioFile = new File(filePath); // 파일 객체 생성
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile); // 오디오 스트림 생성

            clip = AudioSystem.getClip(); // 클립 생성
            clip.open(audioStream); // 스트림 열기

            if (loop) // 반복 여부 확인
                clip.loop(Clip.LOOP_CONTINUOUSLY); // 무한 반복 재생
            else
                clip.start(); // 한 번 재생

        } catch (Exception e) { // 예외 처리
            e.printStackTrace(); // 스택 트레이스 출력
            System.out.println("음악 파일 재생 오류: " + filePath); // 오류 메시지
        }
    }

    public void stop() { // 음악 정지 메서드
        if (clip != null && clip.isRunning()) { // 클립이 존재하고 재생 중이면
            clip.stop(); // 클립 정지
        }
    }
}
