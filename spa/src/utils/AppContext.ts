import { createContext } from 'react';

interface AppContext {
    isLoggedIn: boolean;
    userId: number | null;
    userName: string | null;
    accessToken: string | null;
}

const AppContext = createContext<AppContext>({
    isLoggedIn: false,
    userId: null,
    userName: null,
    accessToken: null,
});

export default AppContext;