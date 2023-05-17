import {createContext, useContext, useState} from "react";
import {AuthContextValue, AuthState} from "@types";

const AUTH_STATE_KEY = "auth_state";

const AUTH_INIT_STATE: AuthState = {
    isLoggedIn: false,
    userId: null,
    userName: null,
    accessToken: null,
};

const AuthContext = createContext<AuthContextValue>({
    authState: AUTH_INIT_STATE,
    updateAuthState: () => {
    },
    loginSuccess: () => {
    },
    loginFailure: () => {
    },
});

function AuthProvider({children}: { children: React.ReactNode }) {
    const [authState, setAuthState] = useState<AuthState>(AUTH_INIT_STATE);

    const updateAuthState = (newAuthState: AuthState) => {
        sessionStorage.setItem(AUTH_STATE_KEY, JSON.stringify(newAuthState));
        setAuthState(newAuthState);
    };

    const loginSuccess = (newAuthState: AuthState) => {
        sessionStorage.setItem(AUTH_STATE_KEY, JSON.stringify(newAuthState));
        setAuthState({...newAuthState, isLoggedIn: true});
    };

    const loginFailure = () => {
        sessionStorage.removeItem(AUTH_STATE_KEY);
        setAuthState(AUTH_INIT_STATE);
    };

    const value = {
        authState,
        updateAuthState,
        loginSuccess,
        loginFailure,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

function useAuthState() {
    const {authState} = useContext(AuthContext);
    return authState;
}

function useAuthDispatch() {
    const {updateAuthState, loginSuccess, loginFailure} = useContext(
        AuthContext
    );
    return {updateAuthState, loginSuccess, loginFailure};
}

export {AuthProvider, useAuthState, useAuthDispatch};
