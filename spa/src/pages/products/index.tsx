import { useState, useEffect } from 'react';
import type { NextPage } from 'next';
import axios from 'axios';
import Link from 'next/link';

import { BaseLayout } from "@ui"
import * as Constants from '@utils/Constants';
import { useAuthState, useAuthDispatch } from '@context';
import { ProductProp } from '@types'

const Product: NextPage = () => {
    const { accessToken } = useAuthState();
    const [products, setProducts] = useState<ProductProp[]>([]);

    useEffect(() => {
        if (!accessToken) {
            console.log('No auth token provided');
            return;
        }

        axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
        axios
            .get(Constants.PRODUCT_SVC_URL)
            .then((response) => {
                console.log(response.data);
                console.log('Product request STATUS = ' + response.status);
                const products = response.data;
                for (let i = 0; i < products.length; i++) {
                    const postfix_char = products[i].id.charAt(products[i].id.length - 1);
                    products[i].imageUrl = Constants.PRODUCT_IMAGE_BASE_URL + postfix_char + '.jpeg';
                    console.log(products[i]);
                }
                setProducts(products);
            })
            .catch((error) => {
                console.log('Error description: ' + error);
                if (error.response && error.response.status) {
                    console.log('Error Status = ' + error.response.status);
                    if (error.response.status === 403) {
                        console.log('Invalid authorization token');
                    }
                } else {
                    console.log('Unable to fetch products');
                }
            });
    }, [accessToken]);

    if (!products || products.length === 0) {
        return (
            <BaseLayout>
                <div className="text-center py-8">
                    <h3 className="text-3xl font-bold">Products</h3>
                    <p>No Products yet.</p>
                </div>
            </BaseLayout>);
    }

    const productsHtml = products.map((product) => (
        <div className="col-sm-6 col-md-4" key={product.id}>
            <div className="thumbnail product-thumbnail">
                <Link href={'/products/' + product.id}>
                    <img className="img-thumbnail img-fluid" src={product.imageUrl} alt={product.name} />
                </Link>
                <div className="product-details">
                    <h3 className="text-xl font-bold capitalize">{product.name}</h3>
                    <p id="name">{product.id}</p>
                    <h4>{product.price} &#36;</h4>
                </div>
            </div>
        </div>
    ));

    return (
        <BaseLayout>
            <div className="text-center py-8">
                <h3 className="text-3xl font-bold">Products</h3>
            </div>
            <div id="product-grid" className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {productsHtml}
            </div>
        </BaseLayout>
    )
}

export default Product;