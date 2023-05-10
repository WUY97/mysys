/* eslint-disable @next/next/no-img-element */
import { useState, useEffect, useRef } from 'react';
import type { NextPage } from 'next';
import axios from 'axios';
import Link from 'next/link';

import { BaseLayout } from "@ui"
import * as Constants from '@utils/Constants';
import { useAuthState } from '@context';
import { CartLine, CartData } from '@types'

const Cart: NextPage = () => {
    const [cart, setCart] = useState<CartData | null>(null);
    const { userId, accessToken } = useAuthState();

    useEffect(() => {
        console.log('*************************');
        if (cart === null) getCart();
    }, []);

    function getCartId() {
        return userId;
    }

    function getCart() {
        if (!accessToken) {
            console.log('No auth token provided');
            return;
        }

        axios.defaults.headers.common['Authorization'] = 'Bearer ' + accessToken;
        axios
            .get(Constants.CART_SVC_URL + getCartId())
            .then((response) => {
                console.log(response.data);
                console.log('Cart request STATUS = ' + response.status);
                return response.data as CartData;
            })
            .then((newCart) => {
                let requests: Promise<any>[] = [];
                newCart.cartLines.map((cartLine: CartLine, index: any) => (requests[index] = axios.get(Constants.PRODUCT_SVC_URL + cartLine.productId)));
                Promise.all(requests).then((responses) => {
                    //responses.map((response, index) => newCart.cartLines[index].product = response.data);
                    for (let i = 0; i < responses.length; i++) {
                        let product = responses[i].data;
                        newCart.cartLines[i].product = product;
                        let postfix_char = product.id.toString().charAt(product.id.toString().length - 1);
                        newCart.cartLines[i].product.imageUrl = Constants.PRODUCT_IMAGE_BASE_URL + postfix_char + '.jpeg';
                    }
                    console.log(newCart);
                    setCart(newCart);
                });
            })
            .catch((error) => {
                console.log('Error description: ' + error);
                if (error.response && error.response.status) {
                    console.log('Error Status = ' + error.response.status);
                    if (error.response.status === 403) {
                        console.log('Invalid authorization token');
                    }
                } else {
                    console.log('Unable to fetch the cart');
                }
            });
    }

    if (!accessToken || !userId) {
        return (<BaseLayout><h1>Login to View Your Cart</h1><Link className="btn btn-info m-1" href='/login'>Login</Link></BaseLayout>)
    }

    const mycart = cart;

    if (!mycart || !mycart.cartLines) {
        return (<BaseLayout><h1>Cart Empty</h1></BaseLayout>);

    }

    const cartHtml = mycart.cartLines.map((cartLine: CartLine) => (
        <div className="col-sm-6 col-md-4" key={cartLine.id}>
            <div className="thumbnail product-thumbnail">
                <Link href={'/products/' + cartLine.productId}>
                    <img className="img-thumbnail img-fluid" src={cartLine.product?.imageUrl} alt="product name" />
                </Link>
                <div className="product-details">
                    <h4 style={{ textTransform: 'capitalize' }}>{cartLine.product?.name}</h4>
                    <p>{cartLine.productId}</p>
                    <h5>Price: {cartLine.product?.price} &#36;</h5>
                    <p>Qty: {cartLine.quantity}</p>
                </div>
            </div>
        </div>
    ));


    return (
        <BaseLayout>
            <div>
                <div className="container">
                    <div style={{ textAlign: "center", padding: "2em 0" }}>
                        <h3>Cart</h3>
                    </div>
                    <div className="row">
                        <div className="col-12">
                            <div className="well">
                                <div id="product-grid" className="row">
                                    {cartHtml}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </BaseLayout>
    );


}

export default Cart