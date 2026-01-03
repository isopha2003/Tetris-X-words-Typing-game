import javax.swing.JPanel; // 패널
import javax.swing.JTextField; // 텍스트 입력창

import java.awt.event.ActionListener; // 액션 이벤트 처리
import java.awt.event.KeyAdapter; // 키 이벤트 편하게 처리
import java.awt.event.KeyEvent; // 키 이벤트 정보
import java.io.FileWriter; // 파일 쓰기
import java.io.IOException; // 입출력 예외 처리
import java.util.Vector; // 동적 배열
import java.awt.event.ActionEvent; // 액션 이벤트 정보

import java.awt.BorderLayout; // 레이아웃
import java.awt.Color; // 색상
import java.awt.Dimension; // 크기


public class GamePanel extends JPanel { // 게임 패널 클래스
	private Vector<WordLabel> wV = new Vector<WordLabel>(); // 쌓인 단어 벡터
	
	private final Object pauseLock = new Object(); // 스레드1 락
	private final Object pauseLock2 = new Object(); // 스레드2 락
	private final Object pauseLock3 = new Object(); // 스레드3 락
	private boolean isPaused = false; // 일시정지 상태
	
	private WordLabel fallingLabel = new WordLabel(); // 단어 라벨1
	private WordLabel easterEggsLabel = new WordLabel(); // 이스터에그 라벨
	private WordLabel secondFallingLabel = new WordLabel(); // 단어 라벨2
	private WordLabel nextFallingLabel = new WordLabel(); // 다음 단어
	private JTextField tf = new JTextField(10); // 입력 필드
	private FallingThread fThread; // 스레드1
	private SecondThread sThread; // 스레드2
	private EasterEggsThread eThread; // 스레드3
	
	private String word; // 단어 문자열1
	private String word2; // 단어 문자열2
	private String word3; // 단어 문자열3

	private int x; // X좌표
	private int mode; // 게임 모드
	private int diff; // 난이도
	private int delay; // 낙하 속도
	private int width; // 패널 넓이
	private int height; // 패널 높이
	private int life = 0; // 틀린 횟수
	private int successCnt = 0; // 성공 횟수
	private int score; // 최종 점수

	private ScorePanel scorePanel = null; // 점수 패널
	private GamePlayPanel gamePlayPanel = new GamePlayPanel(); // 게임 영역
	private InputPanel inputPanel = new InputPanel(); // 입력 영역
	private GameOverDialog gameOverDialog; // 오버 창
	private PauseDialog pauseDialog = new PauseDialog(this); // 일시정지 창
	private MyToolBar toolBar; // 툴바 참조
	
	private MusicPlayer bgm; // 배경 음악
	private EffectSound es = new EffectSound(); // 효과음
	
	private WestSidePanel westPanel; // 좌측 패널
	private NextWordPanel nextWordPanel; // 다음 단어 패널
	
	private String user = null; // 로그인 사용자
	
	private boolean easterEggs = false; // 이스터에그 모드
	
	private GaugeLabel gauge; // 게이지
	
	public GamePanel(ScorePanel scorePanel, GameOverDialog gameOverDialog, WestSidePanel westPanel) { // 생성자
		this.westPanel = westPanel; // 좌측 패널 설정
		this.gameOverDialog = gameOverDialog; // 오버 창 설정
		this.scorePanel = scorePanel; // 점수 패널 설정
		nextWordPanel = westPanel.getNextWordPanel(); // 다음 단어 패널
		setLayout(new BorderLayout()); // 레이아웃 설정
		add(gamePlayPanel, BorderLayout.CENTER); // 게임 영역 추가
		add(inputPanel, BorderLayout.SOUTH); // 입력 영역 추가
	}
	// 게임 초기화
	public void reset() {
		gamePlayPanel.add(fallingLabel); // 라벨 재추가
		gamePlayPanel.add(secondFallingLabel); // 라벨 재추가
		gamePlayPanel.add(easterEggsLabel); // 라벨 재추가
		gamePlayPanel.add(nextFallingLabel); // 라벨 재추가
		life = 0; // 초기화
		successCnt = 0; // 초기화
	}
	public void resetThread() { // 스레드 초기화
		fThread = new FallingThread(); // 스레드1 생성
		if(diff == 1 || diff == 2 || easterEggs == true) { // 난이도 체크
			sThread = new SecondThread(); // 스레드2 생성
		}
		if(easterEggs == true) { // 이스터에그 체크
			eThread = new EasterEggsThread(); // 스레드3 생성
		}
		gauge.resetThread(); // 게이지 스레드 초기화
	}
	public void interruptThread() {
		fThread.interrupt(); // 스레드1 종료
	}
	public void setFallingLabel(WordLabel la) { // 단어 라벨 업데이트
		String nextWord = nextFallingLabel.getWord(); // 다음 단어
		Dimension d = nextFallingLabel.getSize(); // 크기
		int nextColorIndex = nextFallingLabel.getColorIndex(); // 색상
		int nextX = nextFallingLabel.getX(); // X좌표
		la.setWord(nextWord); // 단어 설정
		la.setColor(nextColorIndex); // 색상 설정
		la.setLocation(nextX, 0); // 위치 설정
		la.setSize(d.width + 10, d.height + 30); // 크기 설정
		nextFallingLabel.setLabel(); // 다음 단어 준비
		nextWordPanel.setPanel(nextFallingLabel); // 다음 단어 패널 업데이트
	}
	// 게임 플레이 영역 패널
	class GamePlayPanel extends JPanel {
	    public GamePlayPanel() {
	        setLayout(null); // 절대 위치
	        setBackground(Color.BLACK); // 배경 검정
	        
	        // 크기 변경 리스너
	        addComponentListener(new java.awt.event.ComponentAdapter() {
	            @Override
	            public void componentResized(java.awt.event.ComponentEvent e) {
	                width = getWidth(); // 넓이 저장
	                height = getHeight(); // 높이 저장
	                fallingLabel.setWidth(width); // 넓이 전달
	        		nextFallingLabel.setWidth(width); // 넓이 전달
	        		easterEggsLabel.setWidth(width); // 넓이 전달
	            }
	        });

	        add(fallingLabel); // 라벨 추가
	        add(secondFallingLabel); // 라벨 추가
	        add(easterEggsLabel); // 라벨 추가
	        add(nextFallingLabel); // 라벨 추가
	        secondFallingLabel.setVisible(false); // 초기 숨김
	        nextFallingLabel.setVisible(false); // 초기 숨김
	        easterEggsLabel.setVisible(false); // 초기 숨김
	    }
	}

	// 게임 시작
	public void start(int mode, int diff) {
		gamePlayPanel.add(fallingLabel); // 라벨 재추가
		gamePlayPanel.add(secondFallingLabel); // 라벨 재추가
		gamePlayPanel.add(easterEggsLabel); // 라벨 재추가
		gamePlayPanel.add(nextFallingLabel); // 라벨 재추가
		isPaused = false; // 일시정지 해제
		
		this.mode = mode; // 모드 설정
		this.diff = diff; // 난이도 설정
		
		resetThread(); // 스레드 초기화
		
		switch(diff) { // 딜레이 설정
		case 0: delay = 300; break;
		case 1: delay = 200; break;
		case 2: delay = 100; break;
		}
		
		fallingLabel.setLabel(); // 단어1 준비
		nextFallingLabel.setLabel(); // 다음 단어 준비
		nextWordPanel.setPanel(nextFallingLabel); // 다음 단어 표시
		word = fallingLabel.getWord(); // 단어1 저장
		tf.setText(""); // 입력창 비우기
		fallingLabel.setVisible(true); // 단어1 보이기
		fThread.start(); // 스레드1 시작
		gauge.start(); // 게이지 시작
		if(diff == 1 || diff == 2 || easterEggs == true) { // 난이도 체크
			secondFallingLabel.setLabel(); // 단어2 준비
			word2 = secondFallingLabel.getWord(); // 단어2 저장
			secondFallingLabel.setVisible(true); // 단어2 보이기
			sThread.start(); // 스레드2 시작
		}
		if(easterEggs == true) { // 이스터에그 체크
			easterEggsLabel.setLabel(); // 단어3 준비
			word3 = easterEggsLabel.getWord(); // 단어3 저장
			easterEggsLabel.setVisible(true); // 단어3 보이기
			eThread.start(); // 스레드3 시작
		}
	}
	public void setGaugeLabel(GaugeLabel gauge) {
		this.gauge = gauge; // 게이지 라벨 설정
	}
	public void setToolBar(MyToolBar toolBar) {
		this.toolBar = toolBar; // 툴바 설정
	}
	public void setBgm(MusicPlayer bgm) {
		this.bgm = bgm; // 배경음악 설정
	}
	public void setUser(String user) {
		this.user = user; // 사용자 이름 설정
	}
	public void logout() {
		this.user = null; // 로그아웃
	}
	
	public void pauseGame() {
	    isPaused = true; // 일시정지 설정
	}
	
	public void resumeGame() { // 재개
	    gauge.resumeGame(); // 게이지 재개
		synchronized (pauseLock) { // 락 동기화
	        isPaused = false; // 일시정지 해제
	        pauseLock.notify(); // 스레드1 재개
	    }
		if(diff == 1 || diff == 2) { // 난이도 체크
        	synchronized (pauseLock2) {
        		pauseLock2.notify(); // 스레드2 재개
        	}
        }
		if(easterEggs == true) { // 이스터에그 체크
			synchronized (pauseLock3) {
				pauseLock3.notify(); // 스레드3 재개
			}
		}
	    if(mode == 0) { // 타임어택 모드
	    	scorePanel.resumeGame(); // 타이머 재개
	    }
	}
	
	public void gameOver() { // 게임 오버
		// === 스레드 정지 ===
	    if (fThread != null) fThread.interrupt(); // 스레드1 종료
	    if (sThread != null) sThread.interrupt(); // 스레드2 종료
	    if (eThread != null) eThread.interrupt(); // 스레드3 종료
	    if (gauge != null) { // 게이지 체크
	        gauge.interruptThread(); // 게이지 스레드 종료
	    }
	    // === 쌓여 있던 라벨 제거 ===
	    for (WordLabel label : wV) { // 목록 순회
	        gamePlayPanel.remove(label); // 라벨 제거
	    }
	    wV.clear(); // 목록 비우기

	    // === 현재 떨어지는 라벨 제거 ===
	    gamePlayPanel.remove(fallingLabel); // 라벨 제거
	    gamePlayPanel.remove(secondFallingLabel); // 라벨 제거
	    gamePlayPanel.remove(easterEggsLabel); // 라벨 제거

	    // === 화면 갱신 ===
	    gamePlayPanel.repaint(); // 다시 그리기
	    gamePlayPanel.revalidate(); // 레이아웃 재계산
		
		fThread.interrupt(); // 스레드1 종료 (중복)
		if(diff == 1 || diff == 2) { // 난이도 체크
		sThread.interrupt(); // 스레드2 종료 (중복)
		for (WordLabel label : wV) { // 목록 순회 (중복)
	        gamePlayPanel.remove(label); // 라벨 제거
	    }
		wV.clear(); // 목록 비우기
		gamePlayPanel.repaint(); // 다시 그리기
		gamePlayPanel.revalidate(); // 레이아웃 재계산
		}
		es.play("sounds/gameOver.wav", +6.02f); // 효과음 재생
		bgm.stop(); // BGM 정지
		gameOverDialog.addScore(); // 점수 표시 준비
		gameOverDialog.setLocationRelativeTo(null); // 중앙 위치
		gameOverDialog.setVisible(true); // 오버 창 표시
		gauge.reset(); // 게이지 초기화
		if(user != null) { // 로그인 체크
			try { // 예외 처리 시작
				FileWriter userRankingOut; // 파일 쓰기 객체
				if(mode == 0) { // 타임어택 모드
					userRankingOut = new FileWriter("files/timeAttackRanking.txt", true); // 파일 경로
				}
				else if(mode == 1) { // 점수 모드
					userRankingOut = new FileWriter("files/scoreRanking.txt", true); // 파일 경로
				}
				else {
					return; // 모드 없음
				}
				score = scorePanel.getScore(); // 점수 가져오기
				String line = user + "," + score + "\n"; // 저장 형식
				userRankingOut.write(line); // 파일에 쓰기
				userRankingOut.close(); // 파일 닫기
				if(mode == 0) { // 타임어택 랭킹 갱신
					westPanel.getRankingPanel().getTimeRankingPanel().reset();
				}
				else if (mode == 1) { // 점수 랭킹 갱신
					westPanel.getRankingPanel().getScoreRankingPanel().reset();
				}
			}
			
			catch(IOException e) { // 예외 처리
				
			}
		}
	}
	class EasterEggsThread extends Thread { // 이스터에그 낙하 스레드
		@Override
	    public void run() { // 실행 시작
	        try { // 예외 처리 시작
	            while (!isInterrupted()) { // 반복

	                synchronized (pauseLock3) { // 락 동기화
	                    while (isPaused) { // 일시정지 상태
	                        pauseLock3.wait(); // 대기
	                    }
	                }
	                
	                sleep(delay); // 딜레이

	                int x = easterEggsLabel.getX(); // X 좌표
	                int y = easterEggsLabel.getY(); // Y 좌표

	                boolean hitBottom = (y + easterEggsLabel.getHeight() >= height); // 바닥 충돌
	                boolean hitStack = isCollidingWithStack(easterEggsLabel); // 스택 충돌

	                if (hitBottom || hitStack) { // 충돌 시
	                	easterEggsLabel.setBackground(Color.GRAY); // 회색 배경
	                	if (height <= 0) { // 크기 미설정
	                	    continue; //
	                	}

	                	if (easterEggsLabel.getY() <= 0) { // 상단 충돌
	                		gamePlayPanel.add(easterEggsLabel); // 라벨 추가
                            gameOver(); // 게임 오버
                            return; // 종료
                        }
	                	if (hitBottom) { // 바닥 충돌 시
	                		easterEggsLabel.setLocation(x, height - easterEggsLabel.getHeight()); // 위치 조정
	                	}
	                	if(mode == 0) { // 타임어택 모드
	                		scorePanel.decreaseTime(); // 시간 감소
	                	}
	                    wV.add(easterEggsLabel); // 스택에 추가
	                    gamePlayPanel.add(easterEggsLabel); // 라벨 추가
	                    gamePlayPanel.repaint(); // 다시 그리기

	                    es.play("sounds/fail.wav", +6.02f); // 실패음
	                    gauge.consume(); // 게이지 감소

	                    // 새 라벨 생성/재활용
	                    if(mode == 1) { // 점수 모드
		                    easterEggsLabel = new WordLabel(); // 새 객체
		                    easterEggsLabel.setLabel(); // 라벨 설정
							setFallingLabel(easterEggsLabel); // 라벨 업데이트
							gamePlayPanel.add(easterEggsLabel); // 라벨 추가
							word3 = easterEggsLabel.getWord(); // 단어 업데이트
							tf.setText(""); // 입력창 비우기
		                    continue; // 계속
	                    }
	                    else { // 타임어택 모드
	                    	setFallingLabel(easterEggsLabel); // 라벨 업데이트
	                    	word3= easterEggsLabel.getWord(); // 단어 업데이트
	                    	tf.setText(""); continue; // 계속
	                    }
	                }

	                easterEggsLabel.setLocation(x, y + 10); // 10픽셀 낙하
	            }
	        }
	        catch (InterruptedException e) { // 인터럽트 예외
	            // 정상 종료
	        }
	    }
	}
	class SecondThread extends Thread { // 두 번째 낙하 스레드
		@Override
	    public void run() { // 실행 시작
	        try { // 예외 처리 시작
	            while (!isInterrupted()) { // 반복

	                synchronized (pauseLock2) { // 락 동기화
	                    while (isPaused) { // 일시정지 상태
	                        pauseLock2.wait(); // 대기
	                    }
	                }
	                
	                sleep(delay); // 딜레이

	                int x = secondFallingLabel.getX(); // X 좌표
	                int y = secondFallingLabel.getY(); // Y 좌표

	                boolean hitBottom = (y + secondFallingLabel.getHeight() >= height); // 바닥 충돌
	                boolean hitStack = isCollidingWithStack(secondFallingLabel); // 스택 충돌

	                if (hitBottom || hitStack) { // 충돌 시
	                	secondFallingLabel.setBackground(Color.GRAY); // 회색 배경
	                	if (height <= 0) { // 크기 미설정
	                	    continue; //
	                	}

	                	if (secondFallingLabel.getY() <= 0) { // 상단 충돌
	                		gamePlayPanel.add(secondFallingLabel); // 라벨 추가
                            gameOver(); // 게임 오버
                            return; // 종료
                        }
	                	if (hitBottom) { // 바닥 충돌 시
	                		secondFallingLabel.setLocation(x, height - secondFallingLabel.getHeight()); // 위치 조정
	                	}
	                	if(mode == 0) { // 타임어택 모드
	                		scorePanel.decreaseTime(); // 시간 감소
	                	}
	                    wV.add(secondFallingLabel); // 스택에 추가
	                    gamePlayPanel.add(secondFallingLabel); // 라벨 추가
	                    gamePlayPanel.repaint(); // 다시 그리기

	                    es.play("sounds/fail.wav", +6.02f); // 실패음
	                    gauge.consume(); // 게이지 감소

	                    // 새 라벨 생성/재활용
	                    if(mode == 1) { // 점수 모드
		                    secondFallingLabel = new WordLabel(); // 새 객체
		                    secondFallingLabel.setLabel(); // 라벨 설정
							setFallingLabel(secondFallingLabel); // 라벨 업데이트
							gamePlayPanel.add(secondFallingLabel); // 라벨 추가
							word2 = secondFallingLabel.getWord(); // 단어 업데이트
							tf.setText(""); // 입력창 비우기
		                    continue; // 계속
	                    }
	                    else { // 타임어택 모드
	                    	setFallingLabel(secondFallingLabel); // 라벨 업데이트
	                    	word2 = secondFallingLabel.getWord(); // 단어 업데이트
	                    	tf.setText(""); continue; // 계속
	                    }
	                }

	                secondFallingLabel.setLocation(x, y + 10); // 10픽셀 낙하
	            }
	        }
	        catch (InterruptedException e) { // 인터럽트 예외
	            // 정상 종료
	        }
	    }
	}
	private boolean isCollidingWithStack(WordLabel falling) { // 충돌 감지 함수
	    int fx = falling.getX(); // 단어 X
	    int fy = falling.getY(); // 단어 Y
	    int fh = falling.getHeight(); // 단어 높이

	    for (WordLabel fixed : wV) { // 스택 순회
	        int sx = fixed.getX(); // 고정 단어 X
	        int sy = fixed.getY(); // 고정 단어 Y
	        int sw = fixed.getWidth(); // 고정 단어 넓이
	        int sh = fixed.getHeight(); // 고정 단어 높이

	        // X축 겹침
	        boolean xOverlap = // X축 겹침 조건
	                fx < sx + sw &&
	                fx + falling.getWidth() > sx;

	        // Y축 터치
	        boolean yTouch = // Y축 터치 조건
	                fy + fh >= sy &&
	                fy < sy;

	        if (xOverlap && yTouch) { // 충돌 확인
	            return true; // 충돌 발생
	        }
	    }
	    return false; // 충돌 없음
	}
	// 첫 번째 낙하 스레드
	class FallingThread extends Thread {
	    @Override
	    public void run() { // 실행 시작
	        try { // 예외 처리 시작
	            while (!isInterrupted()) { // 반복

	                synchronized (pauseLock) { // 락 동기화
	                    while (isPaused) { // 일시정지 상태
	                        pauseLock.wait(); // 대기
	                    }
	                }
	                
	                sleep(delay); // 딜레이

	                int x = fallingLabel.getX(); // X 좌표
	                int y = fallingLabel.getY(); // Y 좌표

	                boolean hitBottom = (y + fallingLabel.getHeight() >= height); // 바닥 충돌
	                boolean hitStack = isCollidingWithStack(fallingLabel); // 스택 충돌

	                if (hitBottom || hitStack) { // 충돌 시
	                	fallingLabel.setBackground(Color.GRAY); // 회색 배경
	                	if (height <= 0) { // 크기 미설정
	                	    continue; //
	                	}

	                	if (fallingLabel.getY() <= 0) { // 상단 충돌
	                		gamePlayPanel.add(fallingLabel); // 라벨 추가
                            gameOver(); // 게임 오버
                            return; // 종료
                        }
	                	if (hitBottom) { // 바닥 충돌 시
	                		fallingLabel.setLocation(x, height - fallingLabel.getHeight()); // 위치 조정
	                	}
	                	if(mode == 0) { // 타임어택 모드
	                		scorePanel.decreaseTime(); // 시간 감소
	                	}
	                    wV.add(fallingLabel); // 스택에 추가
	                    gamePlayPanel.add(fallingLabel); // 라벨 추가
	                    gamePlayPanel.repaint(); // 다시 그리기

	                    es.play("sounds/fail.wav", +6.02f); // 실패음
	                    gauge.consume(); // 게이지 감소

	                    // 새 라벨 생성/재활용
	                    if(mode == 1) { // 점수 모드
		                    fallingLabel = new WordLabel(); // 새 객체
		                    fallingLabel.setLabel(); // 라벨 설정
							setFallingLabel(fallingLabel); // 라벨 업데이트
							gamePlayPanel.add(fallingLabel); // 라벨 추가
							word = fallingLabel.getWord(); // 단어 업데이트
							tf.setText(""); // 입력창 비우기
		                    continue; // 계속
	                    }
	                    else { // 타임어택 모드
	                    	setFallingLabel(fallingLabel); // 라벨 업데이트
	                    	word = fallingLabel.getWord(); // 단어 업데이트
	                    	tf.setText(""); continue; // 계속
	                    }
	                }

	                fallingLabel.setLocation(x, y + 10); // 10픽셀 낙하
	            }
	        }
	        catch (InterruptedException e) { // 인터럽트 예외
	            // 정상 종료
	        }
	    }
	}

	public void setEasterEggs(boolean easterEggs) {
		this.easterEggs = easterEggs; // 이스터에그 모드 설정
	}
	
	public void getFocus() {
		tf.setFocusable(true); // 포커스 설정
		tf.requestFocus(); // 포커스 요청
	}
	
	public void speedUp() { // 속도 증가
		es.play("sounds/continuousSuccess.wav", +1.02f); // 성공 효과음
		if (delay <= 50) { // 최소 딜레이 체크
			delay = 50; // 최소 딜레이
		}
		else {
			delay -= 50; // 속도 증가
		}
	}

	
	// 사용자 입력 패널
	class InputPanel extends JPanel {
		public InputPanel() { // 생성자
			setBackground(new Color(40, 40, 77)); // 배경색
			tf.setFocusable(true); // 포커스 설정
			tf.requestFocus(); // 포커스 요청
			tf.addKeyListener(new KeyAdapter() { // 키 리스너
				@Override
				public void keyPressed(KeyEvent e) { // 키 누름
					int code = e.getKeyCode(); // 키 코드
					if(code == KeyEvent.VK_ESCAPE) { // ESC 키
						isPaused = true; // 일시정지
						gauge.pauseGame(); // 게이지 일시정지
						if(mode == 0) { // 타임어택 모드
							scorePanel.pauseGame(); // 타이머 일시정지
						}
						pauseDialog.setLocationRelativeTo(null); // 중앙 위치
						pauseDialog.setVisible(true); // 일시정지 창 표시
					}
				}
			});
			// tf 에 이벤트 리스너
			tf.addActionListener(new ActionListener() { // 액션 이벤트
				@Override
				public void actionPerformed(ActionEvent e) { // 실행
					String inputWord = tf.getText(); // 입력 단어
					
					if(inputWord.equals(word)) { // 단어1 일치
						es.play("sounds/success.wav", +6.02f); // 성공음
						gauge.fill(); // 게이지 증가
						scorePanel.increaseScore(); // 점수 증가
						setFallingLabel(fallingLabel); // 새 단어
						word = fallingLabel.getWord(); // 단어1 업데이트
						tf.setText(""); // 입력창 비우기
						if(mode == 0) { // 타임어택 모드
							scorePanel.increaseTime(); // 시간 증가
						}
					}
					else if(inputWord.equals(word2)) { // 단어2 일치
						es.play("sounds/success.wav", +6.02f); // 성공음
						gauge.fill(); // 게이지 증가
						scorePanel.increaseScore(); // 점수 증가
						setFallingLabel(secondFallingLabel); // 새 단어
						word2 = secondFallingLabel.getWord(); // 단어2 업데이트
						tf.setText(""); // 입력창 비우기
						if(mode == 0) { // 타임어택 모드
							scorePanel.increaseTime(); // 시간 증가
						}
					}
					else if(inputWord.equals(word3)) { // 단어3 일치
						es.play("sounds/success.wav", +6.02f); // 성공음
						gauge.fill(); // 게이지 증가
						scorePanel.increaseScore(); // 점수 증가
						setFallingLabel(easterEggsLabel); // 새 단어
						word3 = easterEggsLabel.getWord(); // 단어3 업데이트
						tf.setText(""); // 입력창 비우기
						if(mode == 0) { // 타임어택 모드
							scorePanel.increaseTime(); // 시간 증가
						}
					}
					// 오타 발생
					else { // 불일치
						es.play("sounds/fail.wav", +6.02f); // 실패음
						gauge.consume(); // 게이지 감소
						// 입력 창 초기화
						tf.setText(""); // 입력창 비우기
						// 타임 어택이라면
						if(mode == 0) { // 타임어택 모드
							scorePanel.decreaseTime(); // 시간 감소
						}
					}	
				}
			});
			add(tf); // 입력 필드 추가
		}
	} 	
}