import javax.swing.JPanel; // JPanel 임포트
import javax.swing.JLabel; // JLabel 임포트
import javax.swing.Box; // Box 임포트
import javax.swing.BoxLayout; // BoxLayout 임포트

import java.awt.Color; // Color 임포트
import java.awt.BorderLayout; // BorderLayout 임포트
import java.awt.Graphics; // Graphics 임포트
import java.awt.GridLayout; // GridLayout 임포트
import java.awt.Dimension; // Dimension 임포트
import java.awt.Font; // Font 임포트

public class ScorePanel extends JPanel { // ScorePanel 클래스 정의
    private GamePanel gamePanel; // 게임 패널 저장

    private int score = 0; // 현재 점수
    private JLabel scoreTitle = new JLabel("SCORE"); // 점수 제목 라벨
    private JLabel scoreLa = new JLabel(Integer.toString(score)); // 점수를 표시하는 JLabel

    private int second; // 남은 시간

    private JLabel timeTitle = new JLabel("Remaining Time: "); // 시간 제목 라벨
    private JLabel timeLa = new JLabel(Integer.toString(second)); // 시간을 표시하는 JLabel

    private int mode = -1; // 게임 모드
    private int diff; // 게임 난이도

    private TimerThread th = new TimerThread(); // 타이머 스레드
    private LifePanel lifePanel = new LifePanel(); // 생명 패널

    private final Object pauseLock = new Object(); // 일시정지 락
    private boolean isPaused = false; // 일시정지 여부

    private GaugeLabel gauge = new GaugeLabel(); // 게이지 라벨

    private boolean easterEggs = false; // 이스터에그 여부

    public ScorePanel() { // 생성자
        setLayout(new BorderLayout()); // 레이아웃 설정
        scoreTitle.setForeground(new Color(232, 31, 31)); // 제목 색상
        scoreLa.setForeground(new Color(232, 31, 31)); // 점수 라벨 색상

        gauge.setScorePanel(this); // 게이지에 점수패널 설정

        add(lifePanel, BorderLayout.NORTH); // 생명 패널 상단 추가

        JPanel scorePanel = new JPanel(); // 점수 패널 생성
        scorePanel.setBackground(new Color(33, 33, 71)); // 배경색 설정
        scorePanel.setLayout(new GridLayout(2, 1)); // 2행 1열 그리드 레이아웃
        scorePanel.add(new ScoreTextPanel()); // 점수 텍스트 패널 추가
        scorePanel.setBackground(new Color(33, 33, 71)); // 배경색 재설정
        scorePanel.add(gauge); // 게이지 추가

        add(scorePanel, BorderLayout.CENTER); // 중앙에 점수 패널 추가
    }

    class ScoreTextPanel extends JPanel { // 점수 텍스트 패널 클래스
        public ScoreTextPanel() { // 생성자
            setBackground(new Color(33, 33, 71)); // 배경색 설정
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // 수직 정렬

            scoreTitle.setAlignmentX(CENTER_ALIGNMENT); // 제목 중앙 정렬
            scoreLa.setAlignmentX(CENTER_ALIGNMENT); // 점수 중앙 정렬

            scoreTitle.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 제목 폰트
            scoreLa.setFont(new Font("맑은 고딕", Font.BOLD, 30)); // 점수 폰트

            add(scoreTitle); // 제목 라벨 추가
            add(Box.createVerticalStrut(5)); // 제목과 점수 사이 간격 조정
            add(scoreLa); // 점수 라벨 추가
        }
    }

    class LifePanel extends JPanel { // 생명 패널 클래스

        public LifePanel() { // 생성자
            setPreferredSize(new Dimension(300, 200)); // 패널 크기 설정
            setBackground(new Color(33, 33, 71)); // 배경색 설정

            timeTitle.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 시간 제목 폰트
            timeTitle.setForeground(new Color(232, 31, 31)); // 시간 제목 색상
            timeTitle.setVisible(false); // 기본 비활성화
            timeLa.setForeground(new Color(232, 31, 31)); // 시간 라벨 색상
            timeLa.setFont(new Font("맑은 고딕", Font.BOLD, 30)); // 시간 라벨 폰트
            timeLa.setVisible(false); // 기본 비활성화
            add(timeTitle); // 제목 라벨 추가
            add(timeLa); // 시간 라벨 추가
        }

        @Override
        protected void paintComponent(Graphics g) { // 패널 그림 그리기
            super.paintComponent(g); // 부모 paint 호출
            timeLa.setVisible(false); // 기본 비활성화
            if (mode == 0) { // 타임어택 모드
                timeLa.setVisible(true); // 시간 라벨 표시
                timeTitle.setVisible(true); // 제목 표시
            } else { // 그 외 모드
                return; // 아무 것도 안 함
            }
        }
    }

    public void pauseGame() { // 게임 일시정지
        isPaused = true; // 상태 변경
    }

    public void resumeGame() { // 게임 재개
        synchronized (pauseLock) { // 동기화 블록
            isPaused = false; // 상태 변경
            pauseLock.notify(); // 대기 스레드 알림
        }
    }

    class TimerThread extends Thread { // 시간을 감소시키는 스레드
        @Override
        public void run() { // 스레드 실행
            while(second > 0) { // 시간 남았을 때
                try { // 예외 처리
                    synchronized (pauseLock) { // 일시정지 블록
                        while(isPaused) { // 일시정지 상태
                            pauseLock.wait(); // 대기
                        }
                    }
                    sleep(1000); // 1초 대기
                    second -= 1; // 시간 감소
                    timeLa.setText(Integer.toString(second)); // 라벨 갱신
                } catch(InterruptedException e) { // 예외 발생
                    e.printStackTrace(); // 오류 출력
                }
            }
            gamePanel.interruptThread(); // 게임 스레드 중단
            gamePanel.gameOver(); // 게임 오버 처리
            return; // 종료
        }
    }

    public void setMode(int mode) { // 모드 설정
        th = new TimerThread(); // 새 타이머 스레드 생성
        this.mode = mode; // 모드 저장
        repaint(); // 패널 갱신
        if (mode == 0) { // 타임어택 모드면
            th.start(); // 타이머 시작
        }
    }

    public void setDiff(int diff) { // 난이도 설정
        this.diff = diff; // 난이도 저장
        setSecond(); // 시간 초기화
    }

    public void setGamePanel(GamePanel gamePanel) { // 게임 패널 설정
        this.gamePanel = gamePanel; // 저장
        gamePanel.setGaugeLabel(gauge); // 게임에 게이지 연결
        gauge.setGamePanel(gamePanel); // 게이지에 게임 패널 설정
    }

    public void increaseScore() { // 점수 증가
        switch(diff) { // 난이도별 점수
            case 0: score += 10; break; // 쉬움
            case 1: score += 12; break; // 보통
            case 2: score += 15; break; // 어려움
        }
        scoreLa.setText(Integer.toString(score)); // 라벨 갱신
    }

    public void additionalScore(int score) { // 추가 점수
        this.score += score; // 점수 더하기
        scoreLa.setText(Integer.toString(this.score)); // 라벨 갱신
    }

    public void decreaseTime() { // 난이도에 따른 시간 감소
        switch(diff) { // 난이도별
            case 0: second -=1; break; // 쉬움
            case 1: second -= 2; break; // 보통
            case 2: second -= 3; break; // 어려움
        }
        timeLa.setText(Integer.toString(second)); // 라벨 갱신
        repaint(); // 화면 갱신
    }

    public void increaseTime() { // 시간 증가
        if(easterEggs == true) { // 이스터에그 모드
            second += 2; // 2초 증가
        } else { // 일반
            second += 3; // 3초 증가
        }
        if(second > 60) { // 최대 60초
            second = 60; // 제한
        }
        timeLa.setText(Integer.toString(second)); // 라벨 갱신
        repaint(); // 화면 갱신
    }

    public int getScore() { // 현재 점수 반환
        return score; // 반환
    }

    public int getSecond() { // 남은 시간 반환
        return second; // 반환
    }

    public void setSecond() { // 난이도별 시간 설정
        switch(diff) { // 난이도별
            case 0: second = 60; break; // 쉬움
            case 1: second = 30; break; // 보통
            case 2: second = 10; break; // 어려움
        }
        if (easterEggs == true) { // 이스터에그 모드
            second = 5; // 5초
        }
        timeLa.setText(Integer.toString(second)); // 라벨 갱신
        repaint(); // 화면 갱신
    }

    public void setEasterEggs(boolean easterEggs) { // 이스터에그 설정
        this.easterEggs = easterEggs; // 저장
    }

    public void reset() { // 점수판 초기화
        score = 0; // 점수 초기화
        scoreLa.setText(Integer.toString(score)); // 라벨 갱신
        setSecond(); // 시간 초기화
        timeTitle.setVisible(false); // 시간 제목 숨기기
    }
}
