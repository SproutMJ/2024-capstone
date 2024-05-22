import create from 'zustand';

interface UserState {
    user: { id: number; userName: string } | null;
    setUser: (user: { id: number; userName: string;}) => void;
    clearUser: () => void;
}

const useUserStore = create<UserState>((set) => ({
    user: null,
    setUser: (user) => set({ user }),
    clearUser: () => set({ user: null }),
}));

export default useUserStore;