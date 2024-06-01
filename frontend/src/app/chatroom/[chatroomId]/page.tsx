'use client'
import React, { useState, useEffect, useCallback } from 'react';
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Header } from "@/components/ui/header";
import axios from "axios";
import { useRouter } from "next/navigation";

interface Message {
    id: number;
    chatType: string;
    isUser: number;
    content: string;
}

interface BoardTitle {
    id: number;
    title: string;
}

export default function ChatRoom({ params }: { params: { chatroomId: number } }) {
    const [chatMessages, setChatMessages] = useState<Message[]>([]);
    const [recipe, setRecipeString] = useState<string>('');
    const [menu, setMenuString] = useState<string>('');
    const router = useRouter();

    const handleRoutingMain = useCallback(() => {
        router.push('/');
    }, [router]);

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

    // 이미지를 가져오는 함수
    const fetchImage = async (chatId: number) => {
        try {
            // 이미지를 가져오는 API 엔드포인트 호출
            const response = await axios.get(`/api/photo/${chatId}`, {
                responseType: 'arraybuffer' // 이미지 데이터를 바이너리로 받음
            });

            // 응답에서 이미지 데이터 추출
            const imageData = Buffer.from(response.data, 'binary').toString('base64');

            // 이미지를 data URI 형식으로 변환하여 반환
            return `data:image/jpeg;base64,${imageData}`;
        } catch (error) {
            console.error('Error fetching image:', error);
            return null;
        }
    };

    // 게시판을 불러오는 함수
    const fetchBoardTitles = async (menu: string): Promise<BoardTitle[]> => {
        try {
            const response = await axios.get('/api/boards', {
                params: {
                    searchKeyword: menu,
                    page: 0,
                    size: 5
                }
            });
            return response.data.boardLists.map((item: BoardTitle) => ({
                id: item.id,
                title: item.title
            }));
        } catch (error) {
            console.error('Error fetching board titles:', error);
            return [];
        }
    };

    useEffect(() => {
        axios.get<Message[]>(`/api/chatroom/${params.chatroomId}`)
            .then(response => {
                const messagesWithBr = response.data.map(message => ({
                    ...message,
                    content: message.content.replace(/\n/g, "<br>")
                }));

                const menuMessages = messagesWithBr.filter(message => message.chatType === "menu");
                const menuString = menuMessages.map(message => message.content.replace(/<br>/g, "\n")).join("\n");
                setMenuString(menuString);

                const recipeMessages = messagesWithBr.filter(message => message.chatType === "recipe");
                const recipeString = recipeMessages.map(message => message.content.replace(/<br>/g, "\n")).join("\n");
                setRecipeString(recipeString);

                const messagesWithComponents = messagesWithBr.map(async (message) => {
                    if (message.chatType === "link") {
                        const boardTitles = await fetchBoardTitles(menuString);
                        const boardLinks = boardTitles.map(board => (
                            `<a href="/boards/${board.id}" target="_blank" rel="noopener noreferrer" style="text-decoration: underline; color: #007bff;">${board.title}</a>`
                        ));
                        const boardLinkContent = boardLinks.length > 0 ?
                            `<div>게시판 검색 결과:<br>${boardLinks.join("<br>")}</div>` :
                            '';

                        const searchLink = `<div>모두의 레시피에서 검색하시겠습니까?<br><a href="${message.content}" target="_blank" rel="noopener noreferrer" style="text-decoration: underline; color: #007bff;">${menuString} 검색하기</a></div>`;

                        return {
                            ...message,
                            content: `${boardLinkContent}${searchLink}`
                        };
                    } else if (message.chatType === "imageUrl") {
                        return {
                            ...message,
                            content: `<img src="${message.content}" alt="Chat Image" className="rounded-lg" />`
                        };
                    } else if (message.chatType === "image") {
                        // 이미지 가져오기
                        const imageData = await fetchImage(params.chatroomId);
                        return {
                            ...message,
                            content: `<img src="${imageData}" alt="Chat Image" style="max-width: 400px; height: auto;" className="rounded-lg" />`
                        };
                    } else if (message.chatType === "boardTitle") {
                        const boardTitles = await fetchBoardTitles(menuString);
                        return {
                            ...message,
                            content: boardTitles.map(board => (
                                `<a href="/boards/${board.id}" target="_blank" rel="noopener noreferrer" style="text-decoration: underline; color: #007bff;">${board.title}</a>`
                            )).join("<br>")
                        };
                    } else {
                        return message;
                    }
                });
                Promise.all(messagesWithComponents).then(setChatMessages);
            })
            .catch(error => {
                alert('비정상적인 접근입니다.');
                handleRoutingMain();
            });
    }, [params.chatroomId, handleRoutingMain]);

    return (
        <>
            <Header />
            <main className="py-8">
                <div className="grid grid-cols-1 gap-6">
                    <Card className="flex flex-col">
                        <CardContent className="flex-1 space-y-4">
                            {chatMessages.map((message, index) => (
                                <div key={index} className={`flex items-start space-x-4 ${message.isUser === 1 ? 'justify-end' : ''}`}>
                                    {message.isUser !== 1 && (
                                        <Avatar>
                                            <AvatarImage alt="@maxleiter" src="/placeholder-avatar.jpg" />
                                            <AvatarFallback>AI</AvatarFallback>
                                        </Avatar>
                                    )}
                                    <div className="flex flex-col space-y-2">
                                        <div className="rounded-lg bg-gray-100 p-4 dark:bg-gray-800"
                                             dangerouslySetInnerHTML={{ __html: message.content }}/>
                                    </div>
                                    {message.isUser === 1 && (
                                        <Avatar>
                                            <AvatarImage alt="@you" src="/placeholder-avatar.jpg" />
                                            <AvatarFallback>나</AvatarFallback>
                                        </Avatar>
                                    )}
                                </div>
                            ))}
                            <div className="flex justify-center mt-4 space-x-4">
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