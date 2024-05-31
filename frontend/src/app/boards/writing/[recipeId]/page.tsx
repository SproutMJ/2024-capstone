'use client'
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Header } from '@/components/ui/header';
import { Textarea } from '@/components/ui/textarea';
import { useRouter } from 'next/navigation';

export default function PostWriting({ params }: { params: { recipeId: number } }) {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const router = useRouter();
    const [recipes, setRecipes] = useState<{ recipeId: number; menu: string; }[]>([]);
    const [selectedRecipe, setSelectedRecipe] = useState<number | null>(null); // 선택된 레시피 ID

    useEffect(() => {
        const recipeId = Number(params.recipeId);
        if (recipeId !== 0) {
            console.log(params.recipeId);
            const fetchRecipeContent = async () => {
                try {
                    const response = await axios.get(`/api/recipe/${params.recipeId}`);
                    setTitle(response.data.menu);
                    setContent(`재료: ${response.data.ingredients.join(', ')}\n레시피:\n${response.data.recipeInfoList.map((info: string, index:number) => `${index + 1}. ${info}`).join('\n')}`);
                } catch (error) {
                    console.error("Error fetching recipe content:", error);
                }
            };
            fetchRecipes();
            fetchRecipeContent();
        } else {
            setTitle('제목을 입력해주세요');
            setContent('내용을 입력해주세요');
        }
    }, [params.recipeId]);

    const handleBoardSave = async () => {
        try {
            const response = await axios.post('/api/boards', {
                title: title,
                content: content,
            });
            router.push('/boards');
        } catch (error) {
            console.error("Error saving board:", error);
        }
    };

    const fetchRecipes = async () => {
        try {
            const response = await axios.get('/api/recipes');
            setRecipes(response.data);
        } catch (error) {
            console.error("Error fetching recipes:", error);
        }
    };

    const handleRecipeSelection = (recipeId: number) => {
        setSelectedRecipe(recipeId);
    };

    const handleRoutingBoards = (recipeId: number) => {
        router.push(`/boards/writing/${recipeId}`);
    };

    return (
        <>
            <Header />
            <main className="py-8 h-[calc(100vh-72px)]">
                <div className="container mx-auto px-4">
                    <div className="grid grid-cols-1 gap-6">
                        <div>
                            <label className="block mb-2 font-bold text-2xl text-white">제목</label>
                            <Input value={title} onChange={(e) => setTitle(e.target.value)} />
                        </div>
                        <div>
                            <label className="block mb-2 font-bold text-2xl text-white">레시피 가져오기</label>
                            <select
                                className="w-full p-2 rounded border border-gray-300 focus:outline-none focus:border-indigo-500 bg-gray-300 text-black"
                                value={selectedRecipe || ''}
                                onChange={(e) => {
                                    const selectedRecipeId = Number(e.target.value);
                                    handleRecipeSelection(selectedRecipeId);
                                    handleRoutingBoards(selectedRecipeId);
                                }}
                            >
                                <option value="">레시피 선택</option>
                                {recipes.map(recipe => (
                                    <option key={recipe.recipeId} value={recipe.recipeId}>{recipe.menu}</option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <label className="block mb-2 font-bold text-2xl text-white">내용</label>
                            <Textarea
                                className="w-full h-full resize-none"
                                style={{minHeight: '200px', maxHeight: '60vh'}}
                                value={content}
                                onChange={(e) => setContent(e.target.value)}
                            />
                        </div>
                        <div className="flex justify-center mt-6">
                            <Button onClick={() => router.push('/boards')} variant="outline">취소</Button>
                            <Button onClick={handleBoardSave}>저장</Button>
                        </div>
                    </div>
                </div>
            </main>
        </>
    )
}