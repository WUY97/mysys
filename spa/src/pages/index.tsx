import { useRouter } from 'next/router';
import Link from 'next/link';

import Home from './home';
import Products from './products';
import Product from './product';
import Cart from './cart';
import Login from './login';

export default function App() {
    const router = useRouter();

    return (
        <main>
            <h1>Hi</h1>
            <Link href='/'>Home</Link>
            <Link href='/products'>Products</Link>
            <Link href='/cart'>Cart</Link>
            <Link href='/login'>Login</Link>
            <Link href='/logout'>Logout</Link>
            <Link href='/products'>Products</Link>

            {router.pathname === '/' || router.pathname === '/home' ? (
                <Home />
            ) : null}
            {router.pathname === '/products' ? <Products /> : null}
            {router.query.id && router.pathname === '/products/[id]' ? (
                <Product />
            ) : null}
            {router.pathname === '/cart' ? <Cart /> : null}
            {router.pathname === '/login' ? <Login /> : null}
            {router.pathname === '/logout' ? <Home /> : null}
        </main>
    );
}
