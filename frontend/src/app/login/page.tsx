/** onSubmit={handleLogin}
* This code was generated by v0 by Vercel.
* @see https://v0.dev/t/omOQPVCaczg
* Documentation: https://v0.dev/docs#integrating-generated-code-into-your-nextjs-app
*/

/** Add fonts into your Next.js project:

import { Inter } from 'next/font/google'

inter({
  subsets: ['latin'],
  display: 'swap',
})

To read more about using these font, please visit the Next.js documentation:
- App Directory: https://nextjs.org/docs/app/building-your-application/optimizing/fonts
- Pages Directory: https://nextjs.org/docs/pages/building-your-application/optimizing/fonts
**/
"use client";
import React from "react";
import { useState } from "react";
import { Label } from "@/components/ui/label"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import {useRouter} from "next/navigation";

export default function Login() {
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const router = useRouter();

  const handleEnterKeyPress = (event: React.KeyboardEvent<HTMLDivElement>) => {
    if (event.key === 'Enter') {
      handleLogin();
    }
  };

  const handleLogin = async () => {
    try {
      const response = await fetch('/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ userName, password }),
      });

      if (!response.ok) {
        throw new Error('Login failed');
      }

      router.push('/');
    } catch (error) {
      console.error('Login error:', error);
      window.alert("정보가 일치하지 않습니다");
    }
  };

  const handleRegister = () => {
    router.push('/signup');
  };

  return (
      <div
          className="flex h-screen w-full items-center justify-center bg-gray-100 px-4 dark:bg-gray-950"
          onKeyDown={handleEnterKeyPress}
      >
        <div className="w-full max-w-md space-y-4 rounded-lg bg-white p-6 shadow-lg dark:bg-gray-900">
          <div className="space-y-2">
            <h2 className="text-2xl font-bold">AI 명종원</h2>
          </div>
          <div className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="id">아이디</Label>
              <Input
                  id="id"
                  placeholder="아이디를 입력해주세요"
                  type="text"
                  value={userName}
                  onChange={(e) => setUserName(e.target.value)}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="password">비밀번호</Label>
              <Input
                  id="password"
                  placeholder="비밀번호를 입력해주세요"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
              />
            </div>
            <div className="flex flex-col gap-2 sm:flex-row">
              <Button className="flex-1" onClick={handleLogin}>로그인</Button>
              <Button className="flex-1" variant="outline" onClick={handleRegister}>회원가입</Button>
            </div>
          </div>
        </div>
      </div>
  );
}