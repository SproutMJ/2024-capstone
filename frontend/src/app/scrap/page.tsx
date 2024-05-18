/**
* This code was generated by v0 by Vercel.
* @see https://v0.dev/t/Ic5zmuoqaVR
* Documentation: https://v0.dev/docs#integrating-generated-code-into-your-nextjs-app
*/

/** Add fonts into your Next.js project:

import { Libre_Franklin } from 'next/font/google'

libre_franklin({
  subsets: ['latin'],
  display: 'swap',
})

To read more about using these font, please visit the Next.js documentation:
- App Directory: https://nextjs.org/docs/app/building-your-application/optimizing/fonts
- Pages Directory: https://nextjs.org/docs/pages/building-your-application/optimizing/fonts
**/
'use client';
import { useRouter } from 'next/navigation';
import React, { useState, useEffect } from "react";
import Modal from "react-modal";
import axios from "axios";
import { Button } from "@/components/ui/button"
import Link from "next/link"
import { CardTitle, CardDescription, CardHeader, CardContent, CardFooter, Card } from "@/components/ui/card"
import {Header} from "@/components/ui/header";

type Recipe = {
  menu: string;
  recipeId: number;
};

type RecipeDetail = {
  menu: string;
  ingredients: string[];
  recipeInfoList: string[];
};

export default function page() {
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const [selectedRecipe, setSelectedRecipe] = useState<RecipeDetail | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [recipeToDelete, setRecipeToDelete] = useState<number | null>(null);

  const handleLogout = async () => {
    try {
      // 로그아웃 요청 보내기
      await axios.post('/api/logout');
      // 로그아웃 후 로그인 페이지로 이동
      router.push('/login');
    } catch (error) {
      console.error('로그아웃 중 오류가 발생했습니다:', error);
    }
  };

  const router = useRouter();

  useEffect(() => {
    const fetchRecipes = async () => {
      try {
        const response = await axios.get("/api/recipes");
        setRecipes(response.data);
      } catch (error) {
        console.error("Error fetching recipes:", error);
      }
    };

    fetchRecipes();
  }, []);

    const handleLearnMoreClick = async (recipeId: number) => {
        try {
            const response = await axios.get(`/api/recipe/${recipeId}`);
            const recipeDetails: RecipeDetail = response.data;

            setSelectedRecipe({
                ...recipeDetails,
                ingredients: [recipeDetails.ingredients.join(', ')],
                recipeInfoList: recipeDetails.recipeInfoList.map((info, index) => `${index + 1}. ${info}`)
            });
        } catch (error) {
            console.error("레시피 상세 정보를 가져오는 동안 오류가 발생했습니다:", error);
        }
    };

  const handleDeleteClick = async () => {
    if (recipeToDelete !== null) {
      try {
        await axios.delete(`/api/recipe/${recipeToDelete}`);
        setRecipes(recipes.filter(recipe => recipe.recipeId !== recipeToDelete));
        closeModal();
      } catch (error) {
        console.error("Error deleting recipe:", error);
      }
    }
  };

  const openModal = (recipeId: number) => {
    setRecipeToDelete(recipeId);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setRecipeToDelete(null);
  };


    return (
      <>
        <Header></Header>
        <main className="py-8">
          <div className="container mx-auto px-4">
            <div className="grid grid-cols-1 gap-6">
              {recipes.map((recipe, index) => (
                  <Card key={index}>
                    <CardHeader>
                      <CardTitle>{recipe.menu}</CardTitle>
                    </CardHeader>
                    <CardContent>
                      {selectedRecipe && selectedRecipe.menu === recipe.menu && (
                          <div>
                            <h2>재료:</h2>
                            <ul>
                              <li>{selectedRecipe.ingredients[0]}</li>
                              <br/>
                            </ul>
                            <h2>조리 순서:</h2>
                            <ol>
                              {selectedRecipe.recipeInfoList.map((info, i) => (
                                  <li key={i}>{info}</li>
                              ))}
                            </ol>
                          </div>
                      )}
                    </CardContent>
                    <CardFooter className="flex justify-between">
                      <Button variant="outline" onClick={() => {
                        if (selectedRecipe && selectedRecipe.menu === recipe.menu) {
                          setSelectedRecipe(null); // 레시피가 이미 열려있을 경우에는 숨기기
                        } else {
                          handleLearnMoreClick(recipe.recipeId); // 레시피가 열려있지 않을 경우에는 자세히 알아보기
                        }
                      }}>
                        {selectedRecipe && selectedRecipe.menu === recipe.menu ? "숨기기" : "레시피 보기"}
                      </Button>
                      <Button variant="outline" className="text-red-500 border-red-500"
                              onClick={() => openModal(recipe.recipeId)}>
                        <TrashIcon className="h-6 w-6"/>
                      </Button>
                    </CardFooter>
                  </Card>
              ))}
            </div>
          </div>
          <div className="fixed bottom-6 right-6"/>

          {/* 삭제 확인 모달 */}
          <Modal
              isOpen={isModalOpen}
              onRequestClose={closeModal}
              contentLabel="레시피 삭제 확인"
              className="fixed inset-0 flex items-center justify-center z-50 outline-none"
              overlayClassName="fixed inset-0 bg-black bg-opacity-70 z-40"
          >
            {/* 모달 내부 스타일을 JSX 내에서 설정 */}
            <div style={{
              backgroundColor: "#333", /* 검은색 배경색 */
              color: "#fff", /* 텍스트 색상 */
              padding: "20px", /* 내부 간격 설정 */
              borderRadius: "8px" /* 모서리 둥글게 설정 */
            }}>
              <h2 className="text-lg font-semibold mb-4">정말 삭제하시겠습니까?</h2>
              <div className="flex justify-end mt-4">
                <Button variant="outline" onClick={closeModal}>취소</Button>
                <Button variant="outline" className="ml-2 text-red-500 border-red-500" onClick={handleDeleteClick}>네</Button>
              </div>
            </div>
          </Modal>
        </main>
      </>
    )
}

function BellIcon(props) {
  return (
      <svg
          {...props}
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
      >
        <path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9"/>
        <path d="M10.3 21a1.94 1.94 0 0 0 3.4 0"/>
      </svg>
  )
}


function ClipboardIcon(props) {
  return (
      <svg
          {...props}
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
      >
        <rect width="8" height="4" x="8" y="2" rx="1" ry="1"/>
        <path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"/>
      </svg>
  )
}


function HomeIcon(props) {
  return (
      <svg
          {...props}
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
      >
        <path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
        <polyline points="9 22 9 12 15 12 15 22"/>
      </svg>
  )
}


function MenuIcon(props) {
  return (
      <svg
          {...props}
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
      >
        <line x1="4" x2="20" y1="12" y2="12"/>
        <line x1="4" x2="20" y1="6" y2="6"/>
        <line x1="4" x2="20" y1="18" y2="18"/>
      </svg>
  )
}


function ScissorsIcon(props) {
  return (
      <svg
          {...props}
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
      >
        <circle cx="6" cy="6" r="3"/>
        <path d="M8.12 8.12 12 12"/>
        <path d="M20 4 8.12 15.88"/>
        <circle cx="6" cy="18" r="3"/>
        <path d="M14.8 14.8 20 20"/>
      </svg>
  )
}


function UserIcon(props) {
  return (
      <svg
          {...props}
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2" />
      <circle cx="12" cy="7" r="4" />
    </svg>
  )
}

function TrashIcon(props) {
  return (
      <svg
          {...props}
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
      >
        <polyline points="3 6 5 6 21 6" />
        <path d="M19 6l-2 14a2 2 0 0 1-2 2H9a2 2 0 0 1-2-2L5 6" />
        <path d="M10 11v6" />
        <path d="M14 11v6" />
      </svg>
  );
}
