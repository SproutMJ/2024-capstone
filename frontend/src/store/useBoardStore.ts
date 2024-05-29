import create from 'zustand';

// 전역 상태 인터페이스 정의
interface BoardState {
    ownBoard: number;
    setOwnBoard: (ownBoard: number) => void;
    clearOwnBoard: () => void;
}

// 전역 상태 생성
const useBoardStore = create<BoardState>((set) => ({
    ownBoard: 0, // 기본값 0으로 설정
    setOwnBoard: (ownBoard) => set({ ownBoard }),
    clearOwnBoard: () => set({ ownBoard: 0 }),
}));

export default useBoardStore;