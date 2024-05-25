'use client'
import React, {useState, useEffect} from 'react';
import {Button} from "@/components/ui/button";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {Input} from "@/components/ui/input";
import {Header} from "@/components/ui/header";
import axios from "axios";
import {useRouter} from "next/navigation";
import Spinner from "@/components/ui/spinner";// 로딩 스피너 컴포넌트

type ChatMessageRequestDto = {
    content: string;
    isUser: number;
    chatType: string;
};

type ChatRoomRequestDto = {
    menu: string;
    messages: ChatMessageRequestDto[];
};

export default function Recommend() {
    const [step, setStep] = useState(0);
    const [ingredients, setIngredients] = useState<string[]>([]);
    const [yesOrNo, setYesOrNo] = useState('아니오');
    const [file, setFile] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [firstIngredients, setFirstIngredients] = useState('');
    const [middleIngredients, setMiddleIngredients] = useState('');
    const [menus, setMenus] = useState<string[]>([]);
    const [menu, setMenu] = useState('');
    const [chatHistory, setChatHistory] = useState<ChatMessageRequestDto[]>([]);
    const [recipeString, setRecipeString] = useState<string>('');
    const [recipeLink, setRecipeLink] = useState<string>(''); // 추가된 상태
    const [isLoading, setIsLoading] = useState(false);  // 로딩 상태 추가

    const router = useRouter();
    const handleRoutingMain = () => {
        router.push('/');
    };

    const handleRoutingScrap = () => {
        router.push('/scrap');
    };

    const handleNextStep = () => {
        setStep(prev => prev + 1);
    };

    const handleModalOpen = () => {
        setYesOrNo("네")
        setMiddleIngredients(ingredients.join(', '))
        setIsModalOpen(true);
    }

    const modalClose = () => {
        setIsModalOpen(false);
    }

    const handleModalSave = async () => {
        modalClose();
        await handleMenuRecommendation();
    }

    const handleMenuRecommendation = async () => {
        setIsLoading(true);  // 로딩 시작
        console.log(middleIngredients)
        try {
            const response = await axios.post('/api/recommendation-menu', middleIngredients);
            const data = response.data;
            setMenus(data);
            handleNextStep();
        } catch (error) {
            console.error('Error getting menu recommendation:', error);
        } finally {
            setIsLoading(false);  // 로딩 종료
        }
    }

    const handleSaveRecipe = async () => {
        const chatRecipeRequestDto = {
            menu,
            recipe: recipeString,
        };
        try {
            const response = await axios.post('/api/recipe/save-chat', chatRecipeRequestDto);
            console.log('Recipe saved successfully:', response.data);
            handleRoutingScrap();
        } catch (error) {
            console.error('Error saving recipe:', error);
        }
    };

    const handleRecipeStringRecommendation = async (menu: string) => {
        setIsLoading(true);  // 로딩 시작
        setMenu(menu);
        const request = {
            menu: menu,
            ingredients: middleIngredients.split(', '),
        };
        try {
            const response = await axios.post('/api/recommendation-recipe-str', request);
            setRecipeString(response.data);
            const newRecipeLink = `https://www.10000recipe.com/recipe/list.html?q=${encodeURIComponent(menu)}`;
            setRecipeLink(newRecipeLink);
            handleNextStep();
        } catch (error) {
            console.error('Error getting recipe string:', error);
        } finally {
            setIsLoading(false);  // 로딩 종료
        }
    }

    const addChatMessage = (content: string, isUser: number, chatType: string) => {
        setChatHistory(prev => [...prev, {content, isUser, chatType}]);
    };

    const handleSaveAllChatMessage = (
        ingredients: string,
        yesOrNo: string,
        middleIngredients: string,
        menus: string,
        menu: string,
        recipeString: string
    ) => {
        addChatMessage("환영합니다! 재료를 인식할 사진을 업로드 해주세요!", 0, 'message');
        addChatMessage("이미지", 1, 'message');
        addChatMessage(`인식된 재료는 다음과 같습니다.\n${ingredients}`, 0, 'message');
        addChatMessage("재료를 추가하거나 수정하시겠습니까?", 0, 'message');
        addChatMessage(`${yesOrNo}`, 1, 'message');
        if (yesOrNo === "네") {
            addChatMessage(`수정된 재료: ${middleIngredients}`, 1, 'message');
        }
        addChatMessage(`해당 재료로 만들 수 있는 음식은 다음과 같습니다.\n${menus}\n어떤 재료의 음식의 레시피를 보시겠습니까?`, 0, 'message');
        addChatMessage(`${menu}`, 1, 'menu');
        addChatMessage(`${menu}의 레시피는 다음과 같습니다.`, 0, 'message');
        addChatMessage(`${recipeString}`, 0, 'recipe');
        addChatMessage(`${recipeLink}`, 0,'link');
    };

    useEffect(() => {
        if (step === 4) {
            handleSaveAllChatMessage(
                firstIngredients,
                yesOrNo,
                middleIngredients,
                menus.join(', '),
                menu,
                recipeString
            );
        }
    }, [step]);

    useEffect(() => {
        if (chatHistory.length > 0 && step === 4) {
            const chatRoomRequestDto: ChatRoomRequestDto = {
                menu,
                messages: chatHistory.map(chat => ({
                    content: chat.content,
                    isUser: chat.isUser,
                    chatType: chat.chatType,
                }))
            };
            console.log(chatRoomRequestDto);

            axios.post('/api/recommendation-save', chatRoomRequestDto)
                .then(response => {
                    console.log('Chat data saved successfully:', response.data);
                })
                .catch(error => {
                    console.error('Error saving chat data:', error);
                });
        }
    }, [chatHistory, step]);

    const fileChange = (event: any) => {
        setFile(event.target.files[0]);
    }

    const handleUpload = async (event: any) => {
        event.preventDefault();
        if (!file) {
            alert('파일을 선택해주세요.');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        try {
            setIsLoading(true);
            const response = await fetch('/api/photo-recognition', {
                method: 'POST',
                body: formData,
            });
            setIsLoading(false);
            if (!response.ok) {
                throw new Error('파일 업로드 실패');
            }

            const result = await response.text();
            setIngredients(result.split(', '));
            setFirstIngredients(result);
            setMiddleIngredients(result);
            console.log(result);
            console.log(middleIngredients);
            // 다음 단계로 이동
            setStep((prevStep) => prevStep + 2);
        } catch (error) {
            console.error('파일 업로드 에러:', error);
        }
    };

    return (
        <>
            <Header />
            <main className="py-8 relative">
                {isLoading && (
                    <div className="absolute inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
                        <Spinner />  {/* 로딩 스피너 */}
                    </div>
                )}
                <div className="grid grid-cols-1 gap-6">
                    <Card className="flex flex-col">
                        <CardHeader>
                            <CardTitle>레시피 추천</CardTitle>
                        </CardHeader>
                        <CardContent className="flex-1 space-y-4">
                            {step >= 0 && (
                                <div className="flex items-start space-x-4">
                                    <Avatar>
                                        <AvatarImage alt="@jaredpalmer" src="/placeholder-avatar.jpg" />
                                        <AvatarFallback></AvatarFallback>
                                    </Avatar>
                                    <div className="flex flex-col space-y-2">
                                        <div className="rounded-lg bg-gray-100 p-4 dark:bg-gray-800">
                                            <p>환영합니다! 재료를 인식할 사진을 업로드 해주세요!</p>
                                        </div>
                                    </div>
                                </div>
                            )}

                            {step >= 0 && (
                                <div className="flex items-start space-x-4 justify-end">
                                    <div className="flex flex-col space-y-2">
                                        <div className="rounded-lg bg-blue-500 text-white p-4">
                                            <p>사진 업로드 하기</p>
                                            <Input type="file" onChange={fileChange} />
                                            <div className="flex justify-end mt-2 space-x-2">
                                                <Button onClick={handleUpload}>보내기</Button>
                                            </div>
                                        </div>
                                    </div>
                                    <Avatar>
                                        <AvatarImage alt="@you" src="/placeholder-avatar.jpg" />
                                        <AvatarFallback>YU</AvatarFallback>
                                    </Avatar>
                                </div>
                            )}

                            {step >= 2 && (
                                <div className="flex items-start space-x-4">
                                    <Avatar>
                                        <AvatarImage alt="@shadcn" src="/placeholder-avatar.jpg" />
                                        <AvatarFallback>CN</AvatarFallback>
                                    </Avatar>
                                    <div className="flex flex-col space-y-2">
                                        <div className="rounded-lg bg-gray-100 p-4 dark:bg-gray-800">
                                            <p>인식된 재료는 다음과 같습니다.</p>
                                            <p>{ingredients.join(', ')}</p>
                                        </div>
                                    </div>
                                </div>
                            )}

                            {step >= 2 && (
                                <div className="flex items-start space-x-4">
                                    <Avatar>
                                        <AvatarImage alt="@maxleiter" src="/placeholder-avatar.jpg" />
                                        <AvatarFallback>ML</AvatarFallback>
                                    </Avatar>
                                    <div className="flex flex-col space-y-2">
                                        <div className="rounded-lg bg-gray-100 p-4 dark:bg-gray-800">
                                            <p>재료를 추가하거나 수정하시겠습니까?</p>
                                        </div>
                                    </div>
                                </div>
                            )}

                            {step >= 2 && (
                                <div className="flex items-start space-x-4 justify-end">
                                    <div className="flex flex-col space-y-2">
                                        <div className="rounded-lg bg-blue-500 text-white p-4">
                                            <div className="flex space-x-2">
                                                <Button onClick={handleModalOpen}>예</Button>
                                                <Button onClick={handleMenuRecommendation}>아니오</Button>
                                            </div>
                                        </div>
                                    </div>
                                    <Avatar>
                                        <AvatarImage alt="@you" src="/placeholder-avatar.jpg" />
                                        <AvatarFallback>YU</AvatarFallback>
                                    </Avatar>
                                </div>
                            )}

                            {isModalOpen && (
                                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
                                    <div className="bg-blue-500 p-8 rounded w-3/4 md:w-1/2 lg:w-1/3">
                                        <h2 className="text-xl mb-4">재료 수정</h2>
                                        <textarea
                                            value={middleIngredients}
                                            onChange={(e) => setMiddleIngredients(e.target.value)}
                                            className="border p-2 mb-4 w-full h-40"
                                            style={{ color: 'black' }}
                                        />
                                        <div className="flex justify-end space-x-2">
                                            <button onClick={handleModalSave} className="bg-blue-500 text-white px-4 py-2 rounded">
                                                저장
                                            </button>
                                            <button onClick={modalClose} className="bg-gray-500 text-white px-4 py-2 rounded">
                                                취소
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            )}

                            {step >= 3 && (
                                <div className="flex items-start space-x-4">
                                    <Avatar>
                                        <AvatarImage alt="@maxleiter" src="/placeholder-avatar.jpg" />
                                        <AvatarFallback>ML</AvatarFallback>
                                    </Avatar>
                                    <div className="flex flex-col space-y-2">
                                        <div className="rounded-lg bg-gray-100 p-4 dark:bg-gray-800">
                                            <p>최종 입력된 재료는 다음과 같습니다.</p>
                                            <p>{middleIngredients}</p>
                                        </div>
                                    </div>
                                </div>
                            )}

                            {step >= 3 && (
                                <div className="flex items-start space-x-4">
                                    <Avatar>
                                        <AvatarImage alt="@maxleiter" src="/placeholder-avatar.jpg" />
                                        <AvatarFallback>ML</AvatarFallback>
                                    </Avatar>
                                    <div className="flex flex-col space-y-2">
                                        <div className="rounded-lg bg-gray-100 p-4 dark:bg-gray-800">
                                            <p>해당 재료로 만들 수 있는 음식은 다음과 같습니다.</p>
                                            <ol>
                                                {menus.map((menu, index) => (
                                                    <li key={index}>{menu}</li>
                                                ))}
                                            </ol>
                                            <p>어떤 재료의 음식의 레시피를 보시겠습니까?</p>
                                        </div>
                                    </div>
                                </div>
                            )}

                            {step >= 3 && (
                                <div className="flex items-start space-x-4 justify-end">
                                    <div className="flex flex-col space-y-2">
                                        <div className="rounded-lg bg-blue-500 text-white p-4">
                                            <ul className="space-y-2">
                                                {menus.map((menu, index) => (
                                                    <li key={index}>
                                                        <Button onClick={() => handleRecipeStringRecommendation(menu)}>
                                                            {menu}
                                                        </Button>
                                                    </li>
                                                ))}
                                            </ul>
                                        </div>
                                    </div>
                                    <Avatar>
                                        <AvatarImage alt="@you" src="/placeholder-avatar.jpg" />
                                        <AvatarFallback>YU</AvatarFallback>
                                    </Avatar>
                                </div>
                            )}

                            {step >= 4 && (
                                <div className="flex items-start space-x-4">
                                    <Avatar>
                                        <AvatarImage alt="@maxleiter" src="/placeholder-avatar.jpg" />
                                        <AvatarFallback>ML</AvatarFallback>
                                    </Avatar>
                                    <div className="flex flex-col space-y-2">
                                        <div className="rounded-lg bg-gray-100 p-4 dark:bg-gray-800" style={{ whiteSpace: 'pre-line' }}>
                                            <p>{menu}의 레시피는 다음과 같습니다.</p>
                                            <p>{recipeString}</p>
                                        </div>
                                    </div>
                                </div>
                            )}

                            {step >= 4 && (
                                <div className="flex items-start space-x-4">
                                    <Avatar>
                                        <AvatarImage alt="@maxleiter" src="/placeholder-avatar.jpg" />
                                        <AvatarFallback>ML</AvatarFallback>
                                    </Avatar>
                                    <div className="flex flex-col space-y-2">
                                        <div className="rounded-lg bg-gray-100 p-4 dark:bg-gray-800">
                                            <p>모두의 레시피에서 검색하시겠습니까?</p>
                                            {/* 모두의 레시피 링크 */}
                                            <a href={recipeLink} target="_blank" rel="noopener noreferrer" className="text-blue-500 hover:underline">
                                                {menu} 레시피 검색하기
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            )}

                            {step >= 4 && (
                                <div className="flex items-start space-x-4 justify-end">
                                    <div className="flex flex-col space-y-2">
                                        <div className="rounded-lg bg-blue-500 text-white p-4">
                                            <div className="space-y-2">
                                                <p>스크랩에 저장하시겠습니까?</p>
                                                <div className="space-x-2">
                                                    <Button onClick={handleSaveRecipe}>예</Button>
                                                    <Button onClick={handleRoutingMain}>아니오</Button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <Avatar>
                                        <AvatarImage alt="@you" src="/placeholder-avatar.jpg" />
                                        <AvatarFallback>YU</AvatarFallback>
                                    </Avatar>
                                </div>
                            )}
                        </CardContent>
                    </Card>
                </div>
                <div className="fixed bottom-6 right-6" />
            </main>
        </>
    );
};
