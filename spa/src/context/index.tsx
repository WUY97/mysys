import { AuthState } from '@types'
import { Provider, useSelector, useDispatch } from 'react-redux'
import { configureStore, createSlice, PayloadAction } from '@reduxjs/toolkit'

const AUTH_STATE_KEY = 'auth_state';

const AUTH_INIT_STATE: AuthState = {
    isLoggedIn: false,
    userId: null,
    userName: null,
    accessToken: null,
}

const authSlice = createSlice({
    name: 'auth',
    initialState: AUTH_INIT_STATE,
    reducers: {
        updateAuthState(state, action: PayloadAction<AuthState>) {
            const authState = action.payload || AUTH_INIT_STATE;
            sessionStorage.setItem(AUTH_STATE_KEY, JSON.stringify(authState));
            state.isLoggedIn = authState.isLoggedIn;
            state.userId = authState.userId;
            state.userName = authState.userName;
            state.accessToken = authState.accessToken;
        },
        loginSuccess(state, action: PayloadAction<AuthState>) {
            const authState = action.payload || AUTH_INIT_STATE;
            sessionStorage.setItem(AUTH_STATE_KEY, JSON.stringify(authState));
            state.isLoggedIn = true;
            state.userId = authState.userId;
            state.userName = authState.userName;
            state.accessToken = authState.accessToken;
        },
        loginFailure(state) {
            state.isLoggedIn = false;
            state.userId = null;
            state.userName = null;
            state.accessToken = null;
        }
    }
})

const store = configureStore({
    reducer: authSlice.reducer,
});

const { actions } = authSlice;

function AuthProvider({ children }: { children: React.ReactNode }) {
    return <Provider store={store}>{children}</Provider>;
}

function useAuthState() {
    return useSelector((state: any) => state);
}

function useAuthDispatch() {
    const dispatch = useDispatch();
    return {
        updateAuthState: (authState: AuthState) => {
            dispatch(actions.updateAuthState(authState));
        },
        loginSuccess: (authState: AuthState) => {
            dispatch(actions.loginSuccess(authState));
        },
        loginFailure: () => {
            dispatch(actions.loginFailure());
        },
    };
}

export { AuthProvider, useAuthState, useAuthDispatch };