/* eslint-disable @next/next/no-img-element */
import {useState} from 'react';
import type {NextPage} from 'next';
import {BaseLayout} from "@ui"
import axios from 'axios';
import {LoginFormState, LoginProps} from '@types';
import {useAuthDispatch, useAuthState} from '@context'
import * as Constants from '@utils/Constants';

const Login: NextPage = (props: LoginProps) => {
    const {isLoggedIn} = useAuthState();
    const authState = useAuthState();

    const dispatch = useAuthDispatch();

    const [result, setResult] = useState<string>('');
    const [userInfo, setUserInfo] = useState<LoginFormState>({name: '', password: ''})

    function handleChange(event: React.ChangeEvent<HTMLInputElement>) {
        const {name, value} = event.target;
        setUserInfo({[name]: value} as Pick<
            LoginFormState,
            keyof LoginFormState
        >);
        event.preventDefault();
    }

    function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        login(userInfo.name, userInfo.password);
    }

    function postLogin(result: boolean) {
        if (result) {
            if (props.appAuthStateHandler) {
                props.appAuthStateHandler(authState);
            }
        } else {
            if (props.appAuthStateHandler) {
                props.appAuthStateHandler(null);
            }
        }
    }

    function login(name: string, password: string) {
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
                return axios.get(
                    Constants.AUTH_SVC_URL +
                    'token/user?access_token=' +
                    accessToken
                );
            })
            .then((response) => {
                console.log('User request STATUS = ' + response.status);
                setResult('User ' +
                    response.data.name +
                    ' with roles: ' +
                    JSON.stringify(response.data.roles));
                dispatch.loginSuccess({
                    isLoggedIn: true,
                    userId: parseInt(body.id),
                    userName: response.data.name,
                    accessToken: response.data.access_token,
                });
                postLogin(true);
            })
            .catch((error) => {
                console.log('Error description: ' + error);
                if (error.response && error.response.status) {
                    console.log('Error Status = ' + error.response.status);
                    if (error.response.status === 403) {
                        setResult('Incorrect credentials');
                    }
                } else {
                    setResult('Login failure');
                }
                dispatch.loginFailure();
                postLogin(false);
            });
    }

    const page = isLoggedIn ? (
        <h1>You have logged in as</h1>
    ) : (
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
                    onSubmit={handleSubmit}
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
                                value={userInfo.name}
                                onChange={handleChange}
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
                                value={userInfo.password}
                                onChange={handleChange}
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
    );
    return (
        <BaseLayout>
            <div className='mt-20'>
                {page}
                <h4>{result}</h4>
            </div>
        </BaseLayout>
    );
}

export default Login;
