/**
* This code was generated by v0 by Vercel.
* @see https://v0.dev/t/xxZQctyMK7c
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
'use client'
import { Button } from "@/components/ui/button"
import Link from "next/link"
import { CardTitle, CardDescription, CardHeader, CardFooter, Card } from "@/components/ui/card"
import {Header} from "@/components/ui/header";
import React, {useEffect, useState} from "react";
import axios from "axios";
import useUserStore from "@/store/useUserStore";
import useBoardStore from '@/store/useBoardStore';

type Board = {
  id: number;
  title: string;
  commentNum: number;
  createdTime: string;
  username: string;
};

export default function Board() {
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [boards, setBoards] = useState<Board[]>([]);
  const {getState} = useUserStore;
  const ownBoard = useBoardStore((state) => state.ownBoard);
  const [searchKeyword, setSearchKeyword] = useState<string>('');

  const getBoards = async (page = 0)=>{
    try {
      let response;
      if(searchKeyword === ''){
        response  = await axios.get('/api/boards', {
          params: {
            page: page,
          }
        });
      }else{
        response  = await axios.get('/api/boards', {
          params: {
            page: page,
            searchKeyword: searchKeyword
          }
        });
      }

      const boards: Board[] = await response.data.boardLists.map((b: any)=>({
        id: b.id,
        title: b.title,
        commentNum: b.commentNum,
        createdTime: b.createdTime,
        username: b.username,
      }));
      setTotalPages((response?.data.totalPages ===0 ?0 : response?.data.totalPages-1));
      setBoards(boards);
      setSearchKeyword(searchKeyword);
    } catch (error){
      console.log(error)
    }
  };

  const getOwnBoards = async (page = 0) => {
    try {
      let response;
      if (searchKeyword === '') {
        response = await axios.get('/api/boards/current-user', {
          params: {
            page: page,
          }
        });
      } else {
        response = await axios.get('/api/boards/current-user', {
          params: {
            page: page,
            searchKeyword: searchKeyword
          }
        });
      }

      const boards: Board[] = await response.data.boardLists.map((b: any) => ({
        id: b.id,
        title: b.title,
        commentNum: b.commentNum,
        createdTime: b.createdTime,
        username: b.username,
      }));
      setTotalPages((response?.data.totalPages === 0 ? 0 : response?.data.totalPages - 1));
      setBoards(boards);
      setSearchKeyword(searchKeyword);
    } catch (error) {
      console.log(error)
    }
  };

  useEffect(() => {
    if (ownBoard === 1) {
      getOwnBoards();
    } else {
      getBoards();
    }
  }, [ownBoard]);

  const handlePrevPage = () => {
    if (ownBoard === 1) {
      getOwnBoards(currentPage - 1);
    } else {
      getBoards(currentPage - 1);
    }
    setCurrentPage(currentPage - 1);
  }

  const handleNextPage = () => {
    if (ownBoard === 1) {
      getOwnBoards(currentPage + 1);
    } else {
      getBoards(currentPage + 1);
    }
    setCurrentPage(currentPage + 1);
  }

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchKeyword(e.target.value);
  };

  return (
    <>
      <Header></Header>
      <main className="py-8">
        <form onSubmit={(event)=>{
          event.preventDefault()
          if (ownBoard === 1) {
            getOwnBoards();
          } else {
            getBoards();
          }
        }} className="mb-6 flex items-center justify-center">
          <input
              type="text"
              value={searchKeyword}
              onChange={handleSearchChange}
              placeholder="검색어를 입력하세요"
              className="border border-gray-300 rounded px-4 py-2 mr-2 flex-grow"
              style={{ maxWidth: '300px', color: 'black' }}
          />
          <Button type="submit" className="flex-shrink-0">
            검색
          </Button>
        </form>
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-1 gap-6">
            {boards.map((board, index)=>(
                <Card className="w-full" key={index}>
                  <CardHeader>
                    <CardTitle>{board.title + ' (' + board.commentNum + ')'}</CardTitle>
                    <CardDescription>작성일: {board.createdTime}</CardDescription>
                    <CardDescription>작성자: {board.username}</CardDescription>
                  </CardHeader>
                  <CardFooter>
                    <Link href={`boards/${board.id}`}><Button variant="outline">자세히 보기</Button></Link>
                  </CardFooter>
                </Card>
            ))}
          </div>
        </div>

        <div className="flex justify-center mt-8">
          <Button onClick={handlePrevPage} disabled={currentPage === 0}>
            이전
          </Button>
          <div className="mx-4">
            {currentPage} / {totalPages}
          </div>
          <Button onClick={handleNextPage} disabled={currentPage === totalPages}>
            다음
          </Button>
        </div>

        <div className="fixed bottom-6 right-6">
          <Link href={'/boards/writing/0'}>
            <Button size="lg">
              <PlusIcon className="h-6 w-6" />
              <span className="sr-only">Add new</span>
            </Button>
          </Link>
        </div>
      </main>
    </>
  )
}

function PlusIcon(props: any) {
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
      <path d="M5 12h14" />
      <path d="M12 5v14" />
    </svg>
  )
}