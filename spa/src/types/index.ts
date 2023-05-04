import { ReactNode } from 'react';
import { NextRouter } from 'next/router';

export interface AuthState {
    isLoggedIn: boolean;
    userId: number | null;
    userName: string | null;
    accessToken: string | null;
}

export interface HomeProps {
    appAuthStateHandler?: (authState: AuthState | null) => void;
}

export interface AppProps {
    router: NextRouter,
    authState: AuthState,
}

export interface LoginProps {
    appAuthStateHandler?: (authState: AuthState | null) => void;
}