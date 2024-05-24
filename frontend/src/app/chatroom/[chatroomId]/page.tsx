'use client'
import React, {useState, useEffect} from 'react';
import {Button} from "@/components/ui/button";
import {Card, CardContent} from "@/components/ui/card";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {Header} from "@/components/ui/header";
import axios from "axios";
import {useRouter} from "next/navigation";

interface Message {
    chatType: string;
    isUser: number;
    content: string;
}

export default function ChatRoom({ params }: { params: { chatroomId: number } }) {
    const [chatMessages, setChatMessages] = useState<Message[]>([]);
    const [recipe, setRecipeString] = useState<string>('');
    const [menu, setMenuString] = useState<string>('');
    const router = useRouter();

    const handleRoutingMain = () => {
        router.push('/');
    };

    const handleRoutingScrap = () => {
        router.push('/scrap');
    };

    const handleSaveRecipe = async () => {
        const chatRecipeRequestDto = {
            menu,
            recipe: recipe,
        };
        try {
            const response = await axios.post('/api/recipe/save-chat', chatRecipeRequestDto);
            console.log('Recipe saved successfully:', response.data);
            handleRoutingScrap();
        } catch (error) {
            console.error('Error saving recipe:', error);
        }
    };


    useEffect(() => {
        // API 호출하여 채팅 메시지 가져오기
        axios.get<Message[]>(`/api/chatroom/${params.chatroomId}`)
            .then(response => {
                // \n을 <br>로 바꿔줌
                const messagesWithBr = response.data.map(message => ({
                    ...message,
                    content: message.content.replace(/\n/g, "<br>")
                }));

                console.log(messagesWithBr);

                // menu 타입의 채팅만 필터링하여 문자열로 합침
                const menuMessages = messagesWithBr.filter(message => message.chatType === "menu");
                const menuString = menuMessages.map(message => message.content.replace(/<br>/g, "\n")).join("\n");
                setMenuString(menuString);

                // recipe 타입의 채팅만 필터링하여 문자열로 합침
                const recipeMessages = messagesWithBr.filter(message => message.chatType === "recipe");
                const recipeString = recipeMessages.map(message => message.content.replace(/<br>/g, "\n")).join("\n");
                setRecipeString(recipeString);

                // 전체 채팅 메시지 설정
                setChatMessages(messagesWithBr);
            })
            .catch(error => {
                alert('비정상적인 접근입니다.');
                handleRoutingMain();
            });
    }, []);


    return (
        <>
            <Header />
            <main className="py-8">
                <div className="grid grid-cols-1 gap-6">
                    <Card className="flex flex-col">
                        <CardContent className="flex-1 space-y-4">
                            {chatMessages.map((message, index) => (
                                <div key={index} className={`flex items-start space-x-4 ${message.isUser === 0 ? 'justify-end' : ''}`}>
                                    <div className="flex flex-col space-y-2">
                                        <div className="rounded-lg bg-gray-100 p-4 dark:bg-gray-800" dangerouslySetInnerHTML={{ __html: message.content }} />
                                    </div>
                                    {message.isUser === 0 && (
                                        <Avatar>
                                            <AvatarImage alt="@you" src="/placeholder-avatar.jpg" />
                                            <AvatarFallback>YU</AvatarFallback>
                                        </Avatar>
                                    )}
                                    {message.isUser !== 0 && (
                                        <Avatar>
                                            <AvatarImage alt="@maxleiter" src="/placeholder-avatar.jpg" />
                                            <AvatarFallback>ML</AvatarFallback>
                                        </Avatar>
                                    )}
                                </div>
                            ))}
                            {/* 마지막에 버튼 추가 */}
                            <div className="flex justify-center mt-4">
                                <Button onClick={handleSaveRecipe}>저장</Button>
                                <Button onClick={handleRoutingMain}>나가기</Button>
                            </div>
                        </CardContent>
                    </Card>
                </div>
            </main>
        </>
    );
}