# Tetris X Word typing game
> 고전 아케이드 게임인 테트리스를 오마주하여 만든 단어 맞추기 게임

## 사용 기술
- **Language**: Java
- **Data Storage**: 별도의 DBMS 없이 파일 시스템을 활용한 데이터 관리
- **Tool**: Eclipse

## 주요 기능
1. **로그인**: 로그인을 통해 순위권 사용자의 점수 데이터 저장
2. **동적 게임 플레이**: 테트리스처럼 하강하는 단어를 입력하여 제거
3. **난이도 시스템**: 난이도에 따른 단어 하강 속도 및 단어 길이 조절
4. **이스터에그**: 특정 조건 만족 시 발동하는 '히든 고난도 모드' 구현

## 실행 화면
| 초기 실행 화면 | 기본 게임 플레이 화면 | 이스터에그 플레이 화면 |
|:---:|:---:|:---:|
|![login](https://github.com/user-attachments/assets/0247575a-e121-4728-96b1-e0b1e89e5bb5)|![play](https://github.com/user-attachments/assets/e5ea4c6f-2f35-4599-bd09-b551b45d2a85)|![easterEggs](https://github.com/user-attachments/assets/800de07d-183c-4bac-b702-61ef9a3de757)



## 핵심 경험
### 1. 텍스트 파일을 이용한 데이터 관리 로직
- 문제: DBMS를 사용하지 않고 사용자 점수를 유지해야 함
- 해결: BufferedWriter와 BufferedReader를 사용하여 프로그램 종료 시 리스트 데이터를 .txt 파일에 저장하고, 이를 실행 시 다시 로드하는 로직을 구현하여 데이터 영속성 확보

### 2. 멀티스레드를 이용한 실시간 게임 처리
- 문제: 단어 하강과 사용자 입력이 동시에 이루어져야 함
- 해결: 단어의 하강을 담당하는 스레드와 사용자 입력을 감지하는 스레드를 분리하여, 끊김 없는 게임 플레이를 구현
