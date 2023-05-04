/* eslint-disable @next/next/no-img-element */
import { Component } from 'react';
import Image from 'next/image';
import axios from 'axios';
import { AuthState, LoginProps } from '@types';

import * as Constants from '@utils/Constants';

interface LoginFormProps {
    loginHandler: (name: string, password: string) => void;
}

interface LoginFormState {
    name: string;
    password: string;
}

class LoginForm extends Component<LoginFormProps, LoginFormState> {
    constructor(props: LoginFormProps) {
        super(props);
        this.state = { name: '', password: 'password' };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event: React.ChangeEvent<HTMLInputElement>) {
        const { name, value } = event.target;
        this.setState({ [name]: value } as Pick<
            LoginFormState,
            keyof LoginFormState
        >);
        event.preventDefault();
    }

    handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        this.props.loginHandler(this.state.name, this.state.password);
    }

    render() {
        return (
            <>
                <div className='flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8'>
                    <div className='sm:mx-auto sm:w-full sm:max-w-sm'>
                        <img
                            className='mx-auto h-10 w-auto'
                            src='https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=600'
                            alt='Your Company'
                        />
                        <h2 className='mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900'>
                            Sign in to your account
                        </h2>
                    </div>

                    <div className='mt-10 sm:mx-auto sm:w-full sm:max-w-sm'>
                        <form
                            className='space-y-6'
                            action='#'
                            method='POST'
                            onSubmit={this.handleSubmit}
                        >
                            <div>
                                <label
                                    htmlFor='name'
                                    className='block text-sm font-medium leading-6 text-gray-900'
                                >
                                    Username
                                </label>
                                <div className='mt-2'>
                                    <input
                                        id='name'
                                        name='name'
                                        type='name'
                                        required
                                        value={this.state.name}
                                        onChange={this.handleChange}
                                        className='block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6'
                                    />
                                </div>
                            </div>

                            <div>
                                <div className='flex items-center justify-between'>
                                    <label
                                        htmlFor='password'
                                        className='block text-sm font-medium leading-6 text-gray-900'
                                    >
                                        Password
                                    </label>
                                </div>
                                <div className='mt-2'>
                                    <input
                                        id='password'
                                        name='password'
                                        type='password'
                                        autoComplete='current-password'
                                        required
                                        value={this.state.password}
                                        onChange={this.handleChange}
                                        className='block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6'
                                    />
                                </div>
                            </div>

                            <div>
                                <button
                                    type='submit'
                                    className='flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600'
                                >
                                    Sign in
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </>
        );
    }
}

class Login extends Component<LoginProps> {
    private authState: AuthState = {
        isLoggedIn: false,
        userId: null,
        userName: null,
        accessToken: null,
    };
    private result: string = '';

    constructor(props: LoginProps) {
        super(props);
        this.login = this.login.bind(this);
    }

    postLogin(result: boolean) {
        if (result) {
            if (this.props.appAuthStateHandler) {
                this.props.appAuthStateHandler(this.authState);
            }
        } else {
            if (this.props.appAuthStateHandler) {
                this.props.appAuthStateHandler(null);
            }
        }
    }

    login(name: string, password: string) {
        const body = {
            id: name,
            password: password,
        };

        console.log(JSON.stringify(body));

        axios.defaults.headers.common['Authorization'] =
            'Basic d2ViLWNsaWVudDpzZWNyZXQ=';

        axios
            .post(Constants.AUTH_SVC_URL + 'token', body)
            .then((response) => {
                console.log(response.data);
                console.log('Token request STATUS = ' + response.status);
                return response.data.access_token;
            })
            .then((accessToken) => {
                console.log('Access token is ' + accessToken);
                this.authState['accessToken'] = accessToken;
                return axios.get(
                    Constants.AUTH_SVC_URL +
                        'token/user?access_token=' +
                        accessToken
                );
            })
            .then((response) => {
                console.log('User request STATUS = ' + response.status);
                this.result =
                    'User ' +
                    response.data.name +
                    ' with roles: ' +
                    JSON.stringify(response.data.roles);
                this.authState.isLoggedIn = true;
                this.authState.userId = parseInt(body.id);

                this.authState.userName = response.data.name;
                this.postLogin(true);
            })
            .catch((error) => {
                console.log('Error description: ' + error);
                if (error.response && error.response.status) {
                    console.log('Error Status = ' + error.response.status);
                    if (error.response.status === 403) {
                        this.result = 'Incorrect credentials';
                    }
                } else {
                    this.result = 'Login failure';
                }
                this.authState['accessToken'] = null;
                this.postLogin(false);
            });
    }

    render() {
        const page = this.authState.isLoggedIn ? (
            <h1>You have logged in as</h1>
        ) : (
            <LoginForm loginHandler={this.login} />
        );
        return (
            <div className='mt-20'>
                {page}
                <h4>{this.result}</h4>
            </div>
        );
    }
}

export default Login;
