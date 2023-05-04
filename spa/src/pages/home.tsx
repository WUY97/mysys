/* eslint-disable @next/next/no-img-element */
import React, { Component, ContextType, Fragment } from 'react';

import { AppContext } from '@utils';
import { HomeProps } from '@types';

class Home extends Component<HomeProps> {
    static contextType = AppContext;
    context!: ContextType<typeof AppContext>;

    render() {
        return (
            <div>
                <h1 className='text-center'>Home</h1>
                <p className='mt-40 text-center break-words'>
                    {JSON.stringify(this.context)}
                </p>
            </div>
        );
    }

    componentDidMount() {
        if (this.context.isLoggedIn && this.props.appAuthStateHandler) {
            this.props.appAuthStateHandler(null);
        }
    }
}

Home.contextType = AppContext;
export default Home;
