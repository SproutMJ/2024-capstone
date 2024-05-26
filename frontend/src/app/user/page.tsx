'use client';
import { useRouter } from 'next/navigation';
import React, { useState, useEffect } from "react";
// @ts-ignore
import Modal from "react-modal";
import axios from "axios";
// @ts-ignore
import { Button } from "@/components/ui/button";
import { CardHeader, CardContent, CardFooter, Card } from "@/components/ui/card";
import { Header } from "@/components/ui/header";
import { UserIcon, HomeIcon, MenuIcon, ClipboardIcon, ScissorsIcon } from "lucide-react";

// UserDetail 타입 정의
type UserDetail = {
    userName: string;
    recipesCount: number;
    chatRoomsCount: number;
    boardsCount: number;
    commentsCount: number;
};

// ChangeUserNameRequestDto 타입 정의
type ChangeUserNameRequestDto = {
    userName: string;
    password: string;
    newUserName: string;
};

// ChangePasswordRequestDto 타입 정의
type ChangePasswordRequestDto = {
    userName: string;
    password: string;
    newPassword: string;
};

type DeleteUserRequestDto = {
    userName: string;
    password: string;
};

export default function UserPage() {
    const router = useRouter();
    const [userDetail, setUserDetail] = useState<UserDetail | null>(null);
    const [isChangeUserNameModalOpen, setChangeUserNameModalOpen] = useState(false);
    const [isChangePasswordModalOpen, setChangePasswordModalOpen] = useState(false);
    const [isDeleteUserModalOpen, setDeleteUserModalOpen] = useState(false);
    const [newUserName, setNewUserName] = useState("");
    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [error, setError] = useState<string>("");

    const handleChangeUserName = async () => {
        if (!newUserName || !currentPassword) {
            setError("모든 필드를 입력하세요.");
            return;
        }

        const data: ChangeUserNameRequestDto = {
            userName: userDetail?.userName || "",
            password: currentPassword,
            newUserName
        };
        try {
            const response = await axios.put('/api/user/change-userName', data);
            if (response.data && response.data.statuscode === 200) {
                setChangeUserNameModalOpen(false);
                setUserDetail(prevDetail => (prevDetail ? { ...prevDetail, userName: newUserName } : null));
                window.alert(response.data.msg); // 성공 메시지 표시
                window.location.href = "/user";
            } else {
                window.alert(response.data.msg); // 실패 메시지 표시
            }
        } catch (error) {
            console.error('Error changing user name:', error);
            window.alert("사용자 이름 변경에 실패했습니다. 다시 시도하세요."); // 실패 메시지 표시
        }
    };

    const handleChangePassword = async () => {
        if (!currentPassword || !newPassword) {
            setError("모든 필드를 입력하세요.");
            return;
        }

        const data: ChangePasswordRequestDto = {
            userName: userDetail?.userName || "",
            password: currentPassword,
            newPassword
        };
        try {
            const response = await axios.put('/api/user/change-password', data);
            if (response.data && response.data.statuscode === 200) {
                setChangePasswordModalOpen(false);
                window.alert(response.data.msg); // 성공 메시지 표시
            } else {
                window.alert(response.data.msg); // 실패 메시지 표시
            }
        } catch (error) {
            console.error('Error changing password:', error);
            window.alert("비밀번호 변경에 실패했습니다. 다시 시도하세요."); // 실패 메시지 표시
        }
    };

    // 삭제 요청 함수
    const handleDeleteUser = async () => {
        if (!currentPassword) {
            setError("비밀번호를 입력하세요.");
            return;
        }

        const data: DeleteUserRequestDto = {
            userName: userDetail?.userName || "",
            password: currentPassword
        };

        try {
            const response = await axios.delete('/api/user', { data });
            if (response.data && response.data.statuscode === 200) {
                setChangeUserNameModalOpen(false);
                window.alert(response.data.msg); // 성공 메시지 표시
                window.location.href = "/login";
            } else {
                window.alert(response.data.msg); // 실패 메시지 표시
            }
        } catch (error) {
            console.error('Error deleting user:', error);
            window.alert("사용자 삭제에 실패했습니다. 다시 시도하세요."); // 실패 메시지 표시
        }
    };

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
            <Header />
            <main className="py-8">
                <div className="container mx-auto px-4">
                    <Card>
                        <CardHeader>
                            <div className="flex justify-center">
                                <UserIcon className="w-32 h-32 text-gray-500" />
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
                                <Button variant="outline" onClick={() => router.push('/my-fridge')}>
                                    나의 냉장고
                                </Button>
                                <Button variant="outline" onClick={() => setChangeUserNameModalOpen(true)}>
                                    사용자 이름 변경
                                </Button>
                                <Button variant="outline" onClick={() => setChangePasswordModalOpen(true)}>
                                    비밀번호 변경
                                </Button>
                                <Button variant="outline" onClick={() => setDeleteUserModalOpen(true)} className="bg-red-500 text-white">
                                    회원 탈퇴
                                </Button>
                            </div>
                        </CardContent>
                        <CardFooter />
                    </Card>
                </div>
            </main>

            {/* 사용자 이름 변경 모달 */}
            <Modal
                isOpen={isChangeUserNameModalOpen}
                onRequestClose={() => setChangeUserNameModalOpen(false)}
                contentLabel="Change User Name"
                className="fixed inset-0 flex items-center justify-center z-50 outline-none"
                overlayClassName="fixed inset-0 bg-black bg-opacity-70 z-40"
            >
                <div style={{ backgroundColor: "#333", color: "#fff", padding: "20px", borderRadius: "8px" }}>
                    <h2 className="text-lg font-semibold mb-4">사용자 이름 변경</h2>
                    <input
                        type="text"
                        placeholder="현재 사용자 이름"
                        value={userDetail?.userName || ""}
                        readOnly
                        className="mb-2 p-2 border rounded w-full text-black bg-gray-200"
                    />
                    <input
                        type="password"
                        placeholder="비밀번호"
                        value={currentPassword}
                        onChange={(e) => setCurrentPassword(e.target.value)}
                        className="mb-2 p-2 border rounded w-full text-black"
                    />
                    <input
                        type="text"
                        placeholder="새 사용자 이름"
                        value={newUserName}
                        onChange={(e) => setNewUserName(e.target.value)}
                        className="mb-2 p-2 border rounded w-full text-black"
                    />
                    <div className="flex justify-end mt-4">
                        <Button variant="outline" onClick={() => setChangeUserNameModalOpen(false)}>취소</Button>
                        <Button variant="outline" className="ml-2 text-green-500 border-green-500" onClick={handleChangeUserName}>변경</Button>
                    </div>
                </div>
            </Modal>

            {/* 비밀번호 변경 모달 */}
            <Modal
                isOpen={isChangePasswordModalOpen}
                onRequestClose={() => setChangePasswordModalOpen(false)}
                contentLabel="Change Password"
                className="fixed inset-0 flex items-center justify-center z-50 outline-none"
                overlayClassName="fixed inset-0 bg-black bg-opacity-70 z-40"
            >
                <div style={{ backgroundColor: "#333", color: "#fff", padding: "20px", borderRadius: "8px" }}>
                    <h2 className="text-lg font-semibold mb-4">비밀번호 변경</h2>
                    <input
                        type="text"
                        placeholder="현재 사용자 이름"
                        value={userDetail?.userName || ""}
                        readOnly
                        className="mb-2 p-2 border rounded w-full text-black bg-gray-200"
                    />
                    <input
                        type="password"
                        placeholder="비밀번호"
                        value={currentPassword}
                        onChange={(e) => setCurrentPassword(e.target.value)}
                        className="mb-2 p-2 border rounded w-full text-black"
                    />
                    <input
                        type="password"
                        placeholder="새 비밀번호"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        className="mb-2 p-2 border rounded w-full text-black"
                    />
                    <div className="flex justify-end mt-4">
                        <Button variant="outline" onClick={() => setChangePasswordModalOpen(false)}>취소</Button>
                        <Button variant="outline" className="ml-2 text-green-500 border-green-500" onClick={handleChangePassword}>변경</Button>
                    </div>
                </div>
            </Modal>
            {/* 사용자 삭제 모달 */}
            <Modal
                isOpen={isDeleteUserModalOpen}
                onRequestClose={() => setDeleteUserModalOpen(false)}
                contentLabel="Delete User"
                className="fixed inset-0 flex items-center justify-center z-50 outline-none"
                overlayClassName="fixed inset-0 bg-black bg-opacity-70 z-40"
            >
                <div style={{ backgroundColor: "#333", color: "#fff", padding: "20px", borderRadius: "8px" }}>
                    <h2 className="text-lg font-semibold mb-4">사용자 삭제</h2>
                    <input
                        type="text"
                        placeholder="사용자 이름"
                        value={userDetail?.userName || ""}
                        readOnly
                        className="mb-2 p-2 border rounded w-full text-black bg-gray-200"
                    />
                    <input
                        type="password"
                        placeholder="비밀번호"
                        value={currentPassword}
                        onChange={(e) => setCurrentPassword(e.target.value)}
                        className="mb-2 p-2 border rounded w-full text-black"
                    />
                    <div className="flex justify-end mt-4">
                        <Button variant="outline" onClick={() => setDeleteUserModalOpen(false)}>취소</Button>
                        <Button variant="outline" className="ml-2 text-red-500 border-red-500" onClick={handleDeleteUser}>삭제</Button>
                    </div>
                </div>
            </Modal>
        </>
    );
}