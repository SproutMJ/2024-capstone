'use client';
import { useRouter } from 'next/navigation';
import axios from 'axios';
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardContent, CardFooter } from "@/components/ui/card";
import { Header } from "@/components/ui/header";
import React, { useEffect, useState } from "react";
import Cookies from 'js-cookie';
import { UserIcon, HomeIcon, MenuIcon, ClipboardIcon, ScissorsIcon } from "lucide-react";

type UserDetail = {
    userName: string;
    recipesCount: number;
    chatRoomsCount: number;
    boardsCount: number;
    commentsCount: number;
};

export default function UserPage() {
    const router = useRouter();
    const [userDetail, setUserDetail] = useState<UserDetail | null>(null);

    const handleLogout = async () => {
        try {
            await axios.post('/api/logout');
            router.push('/login');
        } catch (error) {
            console.error('Error occurred during logout:', error);
        }
    };

    useEffect(() => {
        const token = Cookies.get('auth_token');
        if (!token) {
            router.push('/login');
        }
    }, [router]);

    useEffect(() => {
        const fetchUserDetail = async () => {
            try {
                const response = await axios.get('/api/user-detail');
                setUserDetail(response.data);
            } catch (error) {
                console.error('Error fetching user details:', error);
            }
        };

        fetchUserDetail();
    }, []);
    return (
        <>
            <Header></Header>
            <main className="py-8">
                <div className="container mx-auto px-4">
                    <Card>
                        <CardHeader>
                            <div className="flex justify-center">
                                <UserIcon className="w-32 h-32 text-gray-500"/>
                            </div>
                        </CardHeader>
                        <CardContent>
                            {userDetail && (
                                <div className="text-center mb-4">
                                    <h2 className="text-xl font-semibold">{userDetail.userName}</h2>
                                    <div className="grid grid-cols-2 gap-4">
                                        <Button variant="outline" className="flex items-center justify-center p-2 rounded-lg">
                                            <HomeIcon className="h-5 w-5 text-gray-600" />
                                            <span className="ml-2 text-gray-600">저장된 레시피: {userDetail.recipesCount}</span>
                                        </Button>
                                        <Button variant="outline" className="flex items-center justify-center p-2 rounded-lg">
                                            <MenuIcon className="h-5 w-5 text-gray-600" />
                                            <span className="ml-2 text-gray-600">저장된 채팅방: {userDetail.chatRoomsCount}</span>
                                        </Button>
                                        <Button variant="outline" className="flex items-center justify-center p-2 rounded-lg">
                                            <ClipboardIcon className="h-5 w-5 text-gray-600" />
                                            <span className="ml-2 text-gray-600">작성한 게시글: {userDetail.boardsCount}</span>
                                        </Button>
                                        <Button variant="outline" className="flex items-center justify-center p-2 rounded-lg">
                                            <ScissorsIcon className="h-5 w-5 text-gray-600" />
                                            <span className="ml-2 text-gray-600">작성한 댓글: {userDetail.commentsCount}</span>
                                        </Button>
                                    </div>
                                </div>
                            )}
                            <div className="flex flex-col items-center space-y-4">
                                <Button
                                    variant="outline"
                                    onClick={() => router.push('/change-user-info')}
                                >
                                    회원정보 변경
                                </Button>
                                <Button
                                    variant="outline"
                                    onClick={() => router.push('/my-fridge')}
                                >
                                    나의 냉장고
                                </Button>
                            </div>
                        </CardContent>
                        <CardFooter>
                            {/* Any additional footer content */}
                        </CardFooter>
                    </Card>
                </div>
            </main>
        </>
    );
}
