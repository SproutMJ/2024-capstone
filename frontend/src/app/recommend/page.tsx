'use client'
import React, { useState } from 'react';
import { Button } from "@/components/ui/button";
import { CardTitle, CardHeader, CardContent, Card } from "@/components/ui/card";
import { AvatarImage, AvatarFallback, Avatar } from "@/components/ui/avatar";
import { Input } from "@/components/ui/input";
import { Header } from "@/components/ui/header";
import axios from "axios";
import {useRouter} from "next/navigation";

type Recipe = {
  menu: string;
  ingredients: string[];
  instructions: string[];
};

type Chat = {
  content: string;
  isUser: number;
  chatType: string;
};

export default function Recommend() {
  const [step, setStep] = useState(0);
  const [ingredients, setIngredients] = useState<string[]>([]);
  const [file, setFile] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [middleIngredients, setMiddleIngredients] = useState('');
  const [lastIngredients, setLastIngredients] = useState('');
  const [menus, setMenus] = useState<string[]>([]);
  const [menu, setMenu] = useState('');
  const [recipe, setRecipe] = useState<Recipe>();
  const [instructions, setInstructions] = useState<string[]>([]);

  const router = useRouter();
  const handleRoutingMain = ()=> {
    router.push('/');
  };

  const handleSaveScrap = ()=> {

  };

  const handleNextStep = () => {
    setStep(prev => prev + 1);
  };

  const handleModalOpen = ()=>{
    setMiddleIngredients(ingredients.join(', '))
    setIsModalOpen(true);
  }

  const modalClose = ()=>{
    setIsModalOpen(false);
  }

  const handleModalSave = async ()=>{
    modalClose();
    await handleMenuRecommendation();
  }

  const handleMenuRecommendation = async ()=> {
    const response = await axios.post('/api/recommendation-menu', middleIngredients);
    const data = response.data;
    setMenus(data);
    handleNextStep();
  }

  const handleRecipeRecommendation = async (menu:string)=> {
    setMenu(menu);
    const request = {
      menu: menu,
      ingredients: middleIngredients.split(', '),
    };
    const response = await axios.post('/api/recommendation-recipe', request);

    setLastIngredients(response.data.ingredients.join(', '))
    setInstructions(response.data.instructions)

    const saveRecipeRequestDto = {
      menu: menu,
      ingredients: response.data.ingredients,
      instructions: response.data.instructions,
    }

    await axios.post('/api/save-recipe', saveRecipeRequestDto);

    handleNextStep();
  }

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
      const response = await fetch('/api/photo-recognition', {
        method: 'POST',
        body: formData,
      });

      if (!response.ok) {
        throw new Error('파일 업로드 실패');
      }

      const result = await response.text();
      setIngredients(result.split(', '));
      setMiddleIngredients(ingredients.join(', '));
      // 다음 단계로 이동
      setStep((prevStep) => prevStep + 2);
    } catch (error) {
      console.error('파일 업로드 에러:', error);
    }
  };

  return (
      <>
        <Header />
        <main className="py-8">
          <div className="grid grid-cols-1 gap-6">
            <Card className="flex flex-col">
              <CardHeader>
                <CardTitle>레시피 추천</CardTitle>
              </CardHeader>
              <CardContent className="flex-1 space-y-4">
                {step >= 0 && (
                    <div className="flex items-start space-x-4">
                      <Avatar>
                        <AvatarImage alt="@jaredpalmer" src="/placeholder-avatar.jpg"/>
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
                          <Input type="file" onChange={fileChange}/>
                          <div className="flex justify-end">
                            <Button onClick={handleUpload}>보내기</Button>
                          </div>
                        </div>
                      </div>
                      <Avatar>
                        <AvatarImage alt="@you" src="/placeholder-avatar.jpg"/>
                        <AvatarFallback>YU</AvatarFallback>
                      </Avatar>
                    </div>
                )}

                {step >= 2 && (
                    <div className="flex items-start space-x-4">
                      <Avatar>
                        <AvatarImage alt="@shadcn" src="/placeholder-avatar.jpg"/>
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
                        <AvatarImage alt="@maxleiter" src="/placeholder-avatar.jpg"/>
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
                          <div>
                            <Button onClick={handleModalOpen}>예</Button>
                            <Button onClick={handleMenuRecommendation}>아니오</Button>
                          </div>
                        </div>
                      </div>
                      <Avatar>
                        <AvatarImage alt="@you" src="/placeholder-avatar.jpg"/>
                        <AvatarFallback>YU</AvatarFallback>
                      </Avatar>
                    </div>
                )}

                { isModalOpen && (
                    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
                      <div className="bg-blue-500 p-8 rounded w-3/4 md:w-1/2 lg:w-1/3">
                        <h2 className="text-xl mb-4">재료 수정</h2>
                        <textarea
                            value={middleIngredients}
                            onChange={(e) => setMiddleIngredients(e.target.value)}
                            className="border p-2 mb-4 w-full h-40"
                            style={{color: 'black'}}
                        />
                        <div className="flex justify-end">
                          <button onClick={handleModalSave} className="bg-blue-500 text-white px-4 py-2 rounded mr-2">
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
                        <AvatarImage alt="@maxleiter" src="/placeholder-avatar.jpg"/>
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
                        <AvatarImage alt="@maxleiter" src="/placeholder-avatar.jpg"/>
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
                          <ul>
                            {menus.map((menu, index)=>(
                                <li key={index}><Button onClick={()=>handleRecipeRecommendation(menu)}>{menu}</Button></li>
                            ))}
                          </ul>
                        </div>
                      </div>
                      <Avatar>
                        <AvatarImage alt="@you" src="/placeholder-avatar.jpg"/>
                        <AvatarFallback>YU</AvatarFallback>
                      </Avatar>
                    </div>
                )}

                {step >= 4 && (
                    <div className="flex items-start space-x-4">
                      <Avatar>
                        <AvatarImage alt="@maxleiter" src="/placeholder-avatar.jpg"/>
                        <AvatarFallback>ML</AvatarFallback>
                      </Avatar>
                      <div className="flex flex-col space-y-2">
                        <div className="rounded-lg bg-gray-100 p-4 dark:bg-gray-800">
                          <p>{menu}의 레시피는 다음과 같습니다.</p>
                          <p>재료</p>
                          <ol>
                            {lastIngredients.split(', ').map((val, index) => (
                                <li key={index}>{val}</li>
                            ))}
                          </ol>
                          <p>레시피</p>
                          <ol>
                            {instructions.map((val, index) => (
                                <li key={index}>{val}</li>
                            ))}
                          </ol>
                          <p>스크랩에 저장하시겠습니까?</p>
                        </div>
                      </div>
                    </div>
                )}

                {step >= 4 && (
                    <div className="flex items-start space-x-4 justify-end">
                    <div className="flex flex-col space-y-2">
                        <div className="rounded-lg bg-blue-500 text-white p-4">
                          <div>
                            <Button >예</Button>
                            <Button>아니오</Button>
                          </div>
                        </div>
                      </div>
                      <Avatar>
                        <AvatarImage alt="@you" src="/placeholder-avatar.jpg"/>
                        <AvatarFallback>YU</AvatarFallback>
                      </Avatar>
                    </div>
                )}
              </CardContent>
            </Card>
          </div>
          <div className="fixed bottom-6 right-6"/>
        </main>
      </>
  );
}
