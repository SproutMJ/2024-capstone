import {Sheet, SheetContent, SheetTrigger} from "@/components/ui/sheet";
import {Button} from "@/components/ui/button";
import Link from "next/link";
import {ClipboardIcon, HomeIcon, MenuIcon, ScissorsIcon, UserIcon} from "lucide-react";

export const Header = ()=>{
    return(
    <header className="flex items-center justify-between bg-gray-900 text-white px-4 py-3 shadow-md">
        <div className="flex items-center space-x-4">
            <Sheet>
                <SheetTrigger asChild>
                    <Button size="icon" variant="ghost">
                        <MenuIcon className="h-6 w-6" />
                        <span className="sr-only">Toggle navigation menu</span>
                    </Button>
                </SheetTrigger>
                <SheetContent side="left">
                    <div className="grid gap-4 p-4">
                        <Link
                            className="flex items-center gap-2 rounded-md px-3 py-2 text-sm font-medium hover:bg-gray-800"
                            href="/"
                        >
                            <HomeIcon className="h-5 w-5" />
                            메인페이지
                        </Link>
                        <Link
                            className="flex items-center gap-2 rounded-md px-3 py-2 text-sm font-medium hover:bg-gray-800"
                            href=""
                        >
                            <UserIcon className="h-5 w-5" />
                            마이페이지
                        </Link>
                        <Link
                            className="flex items-center gap-2 rounded-md px-3 py-2 text-sm font-medium hover:bg-gray-800"
                            href="/scrap"
                        >
                            <ScissorsIcon className="h-5 w-5" />
                            스크랩
                        </Link>
                        <Link
                            className="flex items-center gap-2 rounded-md px-3 py-2 text-sm font-medium hover:bg-gray-800"
                            href="/board"
                        >
                            <ClipboardIcon className="h-5 w-5" />
                            게시판
                        </Link>
                    </div>
                </SheetContent>
            </Sheet>
            <Link className="text-lg font-bold" href="#">
                AI 명종원
            </Link>
        </div>
    </header>
    );
};
