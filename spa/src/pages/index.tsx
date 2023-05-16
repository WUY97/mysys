import type {NextPage} from 'next';
import {withRouter} from 'next/router';
import {BaseLayout} from '@ui';
import {useAuthState} from '@context';

const App: NextPage = () => {
    const authState = useAuthState();
    return (
        <BaseLayout>
            <div className='relative bg-gray-50 pt-16 pb-20 px-4 sm:px-6 lg:pt-24 lg:pb-28 lg:px-8'>
                <div className='relative'>
                    <div>
                        <h1 className='text-center'>Home</h1>
                        <p className='mt-40 text-center break-words'>
                            {JSON.stringify(authState)}
                        </p>
                    </div>
                </div>
            </div>
        </BaseLayout>
    );
}

export default withRouter(App);
