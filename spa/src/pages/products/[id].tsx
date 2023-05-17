import {useEffect, useRef, useState} from 'react';
import type {NextPage} from 'next';
import axios from 'axios';
import {useRouter} from 'next/router';
import Link from 'next/link';

import {BaseLayout} from "@ui"
import * as Constants from '@utils/Constants';
import {useAuthState} from '@context';
import {CartLine, ProductProp} from '@types'

const Product: NextPage = () => {
    const {accessToken, userId} = useAuthState();
    const [product, setProduct] = useState<ProductProp>();
    const [remBtnVisible, setRemBtnVisible] = useState<string>('hidden');
    const qtyRef = useRef<HTMLSelectElement>(null);
    const router = useRouter();

    useEffect(() => {
        const getProduct = async (accessToken: string) => {
            if (!accessToken) {
                console.log('No auth token provided');
                return;
            }

            try {
                const productResponse = await axios.get<ProductProp>(
                    Constants.PRODUCT_SVC_URL + router.query.id,
                    {
                        headers: {Authorization: `Bearer ${accessToken}`},
                    }
                );
                console.log(productResponse.data);

                const postfixChar = productResponse.data.id.toString().charAt(productResponse.data.id.toString().length - 1);
                productResponse.data.imageUrl = `${Constants.PRODUCT_IMAGE_BASE_URL}${postfixChar}.jpeg`;

                setProduct(productResponse.data);

                const cartResponse = await axios.get(Constants.CART_SVC_URL + getCartId(), {
                    headers: {Authorization: `Bearer ${accessToken}`},
                });

                console.log(cartResponse.data);

                const cart = cartResponse.data;
                if (cart) {
                    cart.cartLines.forEach((cartLine: CartLine, index: any) => {
                        if (cartLine.productId.toString() === String(productResponse.data.id)) {
                            setRemBtnVisible('visible');
                            return;
                        }
                    });
                }
            } catch (error: any) {
                console.log('Error description: ' + error);
                if (error.response && error.response.status) {
                    console.log('Error Status = ' + error.response.status);
                    if (error.response.status === 403) {
                        console.log('Invalid authorization token');
                    }
                } else {
                    console.log('Unable to fetch product');
                }
            }
        };

        if (accessToken && userId) {
            getProduct(accessToken);
        }
    }, []);

    const getCartId = () => {
        return userId;
    };

    const addToCart = async (cartId: number, productId: number, event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        const qtyNode = qtyRef.current;
        const quantity = qtyNode?.options[qtyNode.selectedIndex].value ?? '1';

        const params = new URLSearchParams();
        params.append('id', cartId.toString());
        params.append('productId', productId.toString());
        params.append('quantity', quantity);

        const config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                Authorization: `Bearer ${accessToken}`,
            },
        };

        try {
            const response = await axios.post(Constants.CART_SVC_URL, params, config);
            console.log(response.data);
            console.log('Product request STATUS = ' + response.status);
            setRemBtnVisible('visible');
        } catch (error: any) {
            console.log('Error description: ' + error);
            if (error.response && error.response.status) {
                console.log('Error Status = ' + error.response.status);
                if (error.response.status === 403) {
                    console.log('Invalid authorization token');
                }
            } else {
                console.log('Unable to add product to the cart');
            }
        }
    }

    const removeFromCart = (cartId: number, productId: number, event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        const params = new URLSearchParams();
        params.append('id', cartId.toString());
        params.append('productId', productId.toString());
        params.append('quantity', '0');
        const config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Authorization': 'Bearer ' + accessToken
            }
        }
        axios.post(Constants.CART_SVC_URL, params, config)
            .then((response) => {
                console.log(response.data);
                console.log('Product request STATUS = ' + response.status);
                setRemBtnVisible('hidden');
            })
            .catch((error) => {
                console.log('Error description: ' + error);
                if (error.response && error.response.status) {
                    console.log('Error Status = ' + error.response.status);
                    if (error.response.status === 403) {
                        console.log('Invalid authorization token');
                    }
                } else {
                    console.log('Unable to remove product from the cart');
                }
            })
    }

    if (!product) {
        return (<BaseLayout><h1 text-3xl font-bold>Product</h1><p>Product does not exist.</p></ BaseLayout>);
    }

    const cartId = getCartId();

    if (!cartId) {
        return (
            <BaseLayout>
                <div className="container">
                    <div className="row">
                        <div className="col-xs-12 col-md-5">
                            <img className='w-full' src={product.imageUrl} alt=""></img>
                        </div>
                        <div className="col-xs-12 col-md-7">
                            <h2 className="text-lg capitalize">{product.name}</h2>
                            <SampleText productName={product.name}/>
                            <div className="form-horizontal">
                                <h3>{product.price} &#36;</h3>
                                <div className="form-group">
                                    <div className="selectContainer p-1">
                                        <select className="form-control" id="qtyAdd{{product.id}}" ref={qtyRef}>
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                        </select>
                                    </div>
                                    <div>
                                        <Link href="/login" className="btn btn-info m-1 float-right">Login to Add to
                                            Cart</Link>
                                        <Link href="/products" className="btn btn-info m-1 float-right">Products</Link>
                                        <Link href="/cart" className="btn btn-success m-1 float-right">Cart</Link>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </BaseLayout>
        )
    }

    return (
        <BaseLayout>
            <div className="container">
                <div className="row">
                    <div className="col-xs-12 col-md-5">
                        <img className='w-full' src={product.imageUrl} alt=""></img>
                    </div>
                    <div className="col-xs-12 col-md-7">
                        <h2 className="text-lg capitalize">{product.name}</h2>
                        <SampleText productName={product.name}/>
                        <div className="form-horizontal">
                            <h3>{product.price} &#36;</h3>
                            <div className="form-group">
                                <div className="selectContainer p-1">
                                    <select className="form-control" id="qtyAdd{{product.id}}" ref={qtyRef}>
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                        <option value="3">3</option>
                                        <option value="4">4</option>
                                        <option value="5">5</option>
                                    </select>
                                </div>
                                <div>
                                    <button onClick={e => addToCart(cartId, product.id, e)}
                                            className="btn btn-primary m-1">
                                        Add to cart
                                    </button>
                                    <button onClick={e => removeFromCart(cartId, product.id, e)}
                                            className={`btn btn-warning mt-1 mb-1 ${remBtnVisible === "hidden" ? "hidden" : ""}`}>
                                        Remove
                                    </button>
                                    <Link href="/products" className="btn btn-info m-1 float-right">Products</Link>
                                    <Link href="/cart" className="btn btn-success m-1 float-right">Cart</Link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </BaseLayout>
    )
}

const SampleText: React.FC<{ productName: String }> = ({productName}) => {
    return (<p>
        {productName}, internationally acclaimed textbook provides a comprehensive introduction to the
        modern study of computer algorithms. It covers a broad range of algorithms in depth, yet makes
        their design and analysis accessible to all levels of readers. Each chapter is relatively
        self-contained and presents an algorithm, a design technique, an application area, or a related
        topic. The algorithms are described and designed in a manner to be readable by anyone who has
        done a little programming. The explanations have been kept elementary without sacrificing depth
        of coverage or mathematical rigor.
    </p>);
}

export default Product;