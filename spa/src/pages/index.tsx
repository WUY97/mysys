import React, { Component, lazy } from 'react';
import { withRouter } from 'next/router';
import { BaseLayout } from '@ui';
import { AuthState, AppProps } from '@types';
import { AppContext } from '@utils';

const Home = lazy(() => import('./home'));
const Login = lazy(() => import('./login'));
const Products = lazy(() => import('./products'));
const Product = lazy(() => import('./product'));
const Cart = lazy(() => import('./cart'));

const AUTH_STATE_KEY = 'auth_state';
const AUTH_INIT_STATE: AuthState = {
    isLoggedIn: false,
    userId: null,
    userName: null,
    accessToken: null,
};

class App extends Component<AppProps> {
    constructor(props: AppProps) {
        super(props);
        this.state = AUTH_INIT_STATE;
        this.updateAppAuthState = this.updateAppAuthState.bind(this);
    }

    updateAppAuthState(authState: AuthState | null) {
        authState = authState ? authState : AUTH_INIT_STATE;
        sessionStorage.setItem(AUTH_STATE_KEY, JSON.stringify(authState));
        this.setState({
            isLoggedIn: authState.isLoggedIn,
            userId: authState.userId,
            userName: authState.userName,
            accessToken: authState.accessToken,
        });
    }

    getAppAuthState() {
        if (typeof sessionStorage !== 'undefined') {
            const state = sessionStorage.getItem(AUTH_STATE_KEY);
            return state ? JSON.parse(state) : AUTH_INIT_STATE;
        } else {
            return AUTH_INIT_STATE;
        }
        
    }

    render() {
        const router = this.props.router;
        const authState = this.getAppAuthState();
        return (
            <AppContext.Provider value={authState}>
                <BaseLayout>
                    <div className='relative bg-gray-50 pt-16 pb-20 px-4 sm:px-6 lg:pt-24 lg:pb-28 lg:px-8'>
                        <div className='absolute inset-0'>
                            <div className='h-1/3 sm:h-2/3' />
                        </div>
                        <div className='relative'>
                            <div className='text-center'>
                                <h2 className='text-3xl tracking-tight font-extrabold text-gray-900 sm:text-4xl'>
                                    Amazing Creatures NFTs
                                </h2>
                                <p className='mt-3 max-w-2xl mx-auto text-xl text-gray-500 sm:mt-4'>
                                    Mint a NFT to get unlimited ownership
                                    forever!
                                </p>
                            </div>
                            {router.pathname === '/' ||
                            router.pathname === '/home' ? (
                                <Home />
                            ) : null}
                            {router.pathname === '/products' ? (
                                <Products />
                            ) : null}
                            {router.query.id &&
                            router.pathname === '/products/[id]' ? (
                                <Product />
                            ) : null}
                            {router.pathname === '/cart' ? <Cart /> : null}
                            {router.pathname === '/login' ? <Login /> : null}
                            {router.pathname === '/logout' ? (
                                <Home
                                    appAuthStateHandler={this.updateAppAuthState.bind(
                                        this
                                    )}
                                />
                            ) : null}
                        </div>
                    </div>
                </BaseLayout>
            </AppContext.Provider>
        );
    }
}

export default withRouter(App);
