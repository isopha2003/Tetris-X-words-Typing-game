import javax.swing.JLabel; // 라벨 클래스
import javax.swing.JPanel; // 패널 클래스

import java.awt.Color; // 색상 클래스
import java.awt.Dimension; // 크기 클래스
import java.awt.Graphics; // 그래픽 그리기

public class GaugeLabel extends JPanel { // 게이지 바 표시 패널

    private GamePanel gamePanel; // 게임 패널 참조

    private Color colors[] = {new Color(52, 184, 178), new Color(73, 129, 197), 
                              new Color(237, 166, 8), new Color(255, 206, 47), 
                              new Color(25, 220, 32), new Color(156, 14, 233),
                              new Color(231, 12, 13)}; // 색상 배열
    private int colorIndex = 0; // 현재 색상 인덱스
    private MyLabel bar = new MyLabel(100); // 게이지 바 라벨
    private ScorePanel scorePanel; // 점수 패널 참조
    private int additionalScore = 10; // 점수 증가량

    private ConsumerThread cThread; // 바 소비 스레드

    private Object pauseLock = new Object(); // 스레드 일시정지용 락
    private boolean isPaused = false; // 일시정지 상태

    public GaugeLabel() { // 생성자
        setLayout(null); // 레이아웃 없음
        bar.setBackground(Color.WHITE); // 바 배경색
        bar.setOpaque(true); // 투명 아님
        bar.setLocation(20, 50); // 위치 설정
        bar.setSize(300, 20); // 크기 설정
        add(bar); // 패널에 바 추가
        setBackground(new Color(33, 33, 71)); // 배경색 설정
        setPreferredSize(new Dimension(350, 200)); // 기본 크기
        setVisible(true); // 패널 보이기
        setFocusable(true); // 포커스 가능
        requestFocus(); // 포커스 요청
        cThread = new ConsumerThread(bar); // 스레드 생성
    }

    class MyLabel extends JLabel { // 게이지 바 라벨 클래스
        private int barSize = 0; // 현재 바 크기
        private int maxBarSize; // 최대 바 크기

        public MyLabel(int maxBarSize) { // 생성자
            this.maxBarSize = maxBarSize; // 최대 크기 설정
        }

        @Override
        public void paintComponent(Graphics g) { // 바 그리기
            super.paintComponent(g); // 기본 그리기
            if(colorIndex > 6) { // 색상 범위 제한
                colorIndex = 6;
            }
            g.setColor(colors[colorIndex]); // 색상 설정
            int width = (int)(((double)(this.getWidth())) / maxBarSize * barSize); // 바 폭 계산
            if(width == 0) { // 0이면 그리지 않음
                return;
            }
            g.fillRect(0, 0, width, this.getHeight()); // 바 그리기
        }

        synchronized public void fill() { // 바 증가
            if(barSize >= maxBarSize) { // 바 최대 도달 시
                gamePanel.speedUp(); // 게임 속도 증가
                scorePanel.additionalScore(additionalScore); // 점수 추가
                additionalScore += 10; // 점수 증가량 증가
                if (colorIndex <= 6) { 
                    setBackground(colors[colorIndex]); // 배경색 변경
                    colorIndex++; // 색상 변경
                    barSize = 0; // 바 초기화
                }
            }
            if (colorIndex <= 6 && barSize <= 100) { // 정상 범위
                barSize += 40; // 바 증가
                repaint(); // 다시 그리기
            }
        }

        synchronized public void consume() { // 바 감소
            if(barSize <= 0) { // 바 0 이하
                if(colorIndex > 0) { // 색상 인덱스 양수
                    if(colorIndex >= 2) { 
                        additionalScore -= 10; // 점수 감소
                        colorIndex -= 2; // 색상 감소
                        setBackground(colors[colorIndex]); // 색상 변경
                        barSize = 100; // 바 최대
                        colorIndex++; // 색상 증가
                    } else { 
                        setBackground(Color.WHITE); // 흰색
                        barSize = 100; // 바 최대
                        colorIndex--; // 색상 감소
                    }
                }
            }
            if(barSize > 0) { // 바 남아있으면 감소
                barSize -= 1; 
                repaint(); // 다시 그리기
            }
        }

        synchronized public void consumeBarSize() { // 일정량 감소
            if(colorIndex >= 0 && barSize >= 20) {
                barSize -= 20; // 20 감소
            } else {
                barSize = 0; // 최소 0
            }
        }

        synchronized public void reset() { // 바 초기화
            colorIndex = 0; // 색상 초기화
            barSize = 0; // 크기 초기화
            additionalScore = 10; // 점수 초기화
            setBackground(Color.WHITE); // 배경색 초기화
            repaint(); // 다시 그리기
        }
    }

    public void pauseGame() { // 게임 일시정지
        isPaused = true;
    }

    public void resumeGame() { // 게임 재개
        synchronized(pauseLock) { 
            isPaused = false; // 상태 변경
            pauseLock.notify(); // 스레드 깨우기
        }
    }

    public void interruptThread() { // 스레드 중단
        if(cThread != null) {
            cThread.interrupt(); // 스레드 인터럽트
        }
    }

    public void resetThread() { // 스레드 재생성
        cThread = new ConsumerThread(bar); 
    }

    public void start() { // 스레드 시작
        cThread.start(); 
    }

    class ConsumerThread extends Thread { // 바 감소 스레드
        private MyLabel bar; // 게이지 라벨

        public ConsumerThread(MyLabel bar) { // 생성자
            this.bar = bar; // 바 설정
        }

        @Override
        public void run() { // 스레드 실행
            while(true) { 
                try {
                    synchronized(pauseLock) { // 일시정지 대기
                        while(isPaused) {
                            pauseLock.wait(); 
                        }
                    }
                    sleep(200); // 0.2초 대기
                    bar.consume(); // 바 감소
                } catch(InterruptedException e) { 
                    return; // 종료
                }
            }
        }
    }

    public void setGamePanel(GamePanel gamePanel) { // 게임 패널 설정
        this.gamePanel = gamePanel; 
    }

    public void setScorePanel(ScorePanel scorePanel) { // 점수 패널 설정
        this.scorePanel = scorePanel; 
    }

    public void fill() { // 바 증가 호출
        bar.fill(); 
    }

    public void consume() { // 바 일정량 감소 호출
        bar.consumeBarSize(); 
    }

    public void reset() { // 바 초기화 호출
        bar.reset(); 
    }
}
