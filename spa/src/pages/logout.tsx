import {useRouter} from 'next/router';
import type {NextPage} from 'next';
import {useEffect} from 'react';
import {BaseLayout} from '@/components/ui';

const Logout: NextPage = () => {
    const router = useRouter();
    useEffect(() => {
        const timer = setTimeout(() => {
            router.push('/');
        }, 5000); // wait 5 seconds before redirecting

        return () => clearTimeout(timer);
    }, [router]);

    return (<BaseLayout><h1>Logging out...</h1></BaseLayout>)
}

export default Logout;