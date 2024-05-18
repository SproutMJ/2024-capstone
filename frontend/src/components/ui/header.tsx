import {Sheet, SheetContent, SheetTrigger} from "@/components/ui/sheet";
import {Button} from "@/components/ui/button";
import Link from "next/link";
import {
    DropdownMenu,
    DropdownMenuContent, DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
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
                            href="#"
                        >
                            <HomeIcon className="h-5 w-5" />
                            Home
                        </Link>
                        <Link
                            className="flex items-center gap-2 rounded-md px-3 py-2 text-sm font-medium hover:bg-gray-800"
                            href="#"
                        >
                            <UserIcon className="h-5 w-5" />
                            My Page
                        </Link>
                        <Link
                            className="flex items-center gap-2 rounded-md px-3 py-2 text-sm font-medium hover:bg-gray-800"
                            href="#"
                        >
                            <ScissorsIcon className="h-5 w-5" />
                            Scrap
                        </Link>
                        <Link
                            className="flex items-center gap-2 rounded-md px-3 py-2 text-sm font-medium hover:bg-gray-800"
                            href="#"
                        >
                            <ClipboardIcon className="h-5 w-5" />
                            Bulletin Board
                        </Link>
                    </div>
                </SheetContent>
            </Sheet>
            <Link className="text-lg font-bold" href="#">
                My App
            </Link>
        </div>
        <div className="flex items-center space-x-4">
            <DropdownMenu>
                <DropdownMenuTrigger asChild />
                <DropdownMenuContent align="end">
                    <DropdownMenuLabel>Notifications</DropdownMenuLabel>
                    <DropdownMenuSeparator />
                    <DropdownMenuItem>
                        <div className="flex flex-col space-y-2">
                            <div className="flex items-center justify-between">
                                <div>
                                    <p className="font-medium">New message</p>
                                    <p className="text-sm text-gray-500">You have a new message</p>
                                </div>
                                <span className="text-xs text-gray-500">5 min ago</span>
                            </div>
                        </div>
                    </DropdownMenuItem>
                    <DropdownMenuItem>
                        <div className="flex flex-col space-y-2">
                            <div className="flex items-center justify-between">
                                <div>
                                    <p className="font-medium">Mention in a post</p>
                                    <p className="text-sm text-gray-500">You were mentioned in a post</p>
                                </div>
                                <span className="text-xs text-gray-500">1 hour ago</span>
                            </div>
                        </div>
                    </DropdownMenuItem>
                    <DropdownMenuItem>
                        <div className="flex flex-col space-y-2">
                            <div className="flex items-center justify-between">
                                <div>
                                    <p className="font-medium">New follower</p>
                                    <p className="text-sm text-gray-500">You have a new follower</p>
                                </div>
                                <span className="text-xs text-gray-500">2 days ago</span>
                            </div>
                        </div>
                    </DropdownMenuItem>
                </DropdownMenuContent>
            </DropdownMenu>
            <DropdownMenu>
                <DropdownMenuTrigger asChild>
                    <Button size="icon" variant="ghost">
                        <img
                            alt="Avatar"
                            className="rounded-full"
                            height={24}
                            src="/placeholder.svg"
                            style={{
                                aspectRatio: "24/24",
                                objectFit: "cover",
                            }}
                            width={24}
                        />
                        <span className="sr-only">User menu</span>
                    </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                    <DropdownMenuLabel>Signed in as John Doe</DropdownMenuLabel>
                    <DropdownMenuSeparator />
                    <DropdownMenuItem>
                        <Link href="#">Profile</Link>
                    </DropdownMenuItem>
                    <DropdownMenuItem>
                        <Link href="#">Settings</Link>
                    </DropdownMenuItem>
                    <DropdownMenuSeparator />
                    <DropdownMenuItem>
                        <Link href="#">Logout</Link>
                    </DropdownMenuItem>
                </DropdownMenuContent>
            </DropdownMenu>
        </div>
    </header>
    );
};
