/**
* This code was generated by v0 by Vercel.
* @see https://v0.dev/t/V0jUKKZKq7P
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
import { Button } from "@/components/ui/button"
import { SheetTrigger, SheetContent, Sheet } from "@/components/ui/sheet"
import Link from "next/link"
import { DropdownMenuTrigger, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuItem, DropdownMenuContent, DropdownMenu } from "@/components/ui/dropdown-menu"
import { Textarea } from "@/components/ui/textarea"

export default function page() {
  return (
    <>
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
                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">New message</p>
                    <p className="text-sm text-gray-500">You have a new message</p>
                  </div>
                  <span className="text-xs text-gray-500">5 min ago</span>
                </div>
              </DropdownMenuItem>
              <DropdownMenuItem>
                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">Mention in a post</p>
                    <p className="text-sm text-gray-500">You were mentioned in a post</p>
                  </div>
                  <span className="text-xs text-gray-500">1 hour ago</span>
                </div>
              </DropdownMenuItem>
              <DropdownMenuItem>
                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">New follower</p>
                    <p className="text-sm text-gray-500">You have a new follower</p>
                  </div>
                  <span className="text-xs text-gray-500">2 days ago</span>
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
      <main className="py-8">
        <div className="container mx-auto px-4">
          <div>
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6" />
          </div>
          <div className="mt-8">
            <div className="bg-white dark:bg-gray-900 rounded-lg shadow-md p-6">
              <div className="flex items-center justify-between mb-4">
                <div className="flex items-center space-x-4">
                  <img
                    alt="Avatar"
                    className="rounded-full"
                    height={40}
                    src="/placeholder.svg"
                    style={{
                      aspectRatio: "40/40",
                      objectFit: "cover",
                    }}
                    width={40}
                  />
                  <div>
                    <h3 className="font-medium text-gray-900 dark:text-gray-100">John Doe</h3>
                    <p className="text-sm text-gray-500 dark:text-gray-400">Posted on May 9, 2024</p>
                  </div>
                </div>
                <div className="flex items-center space-x-2">
                  <Button size="icon" variant="ghost">
                    <BookmarkIcon className="h-5 w-5" />
                    <span className="sr-only">Bookmark</span>
                  </Button>
                  <Button size="icon" variant="ghost">
                    <ShareIcon className="h-5 w-5" />
                    <span className="sr-only">Share</span>
                  </Button>
                </div>
              </div>
              <div className="mb-4">
                <h2 className="text-2xl font-bold text-gray-900 dark:text-gray-100 mb-2">
                  The Ultimate Guide to Tailwind CSS
                </h2>
                <p className="text-gray-700 dark:text-gray-400">
                  Tailwind CSS is a utility-first CSS framework that allows you to quickly build custom user interfaces.
                  In this guide, we'll cover everything you need to know to get started with Tailwind CSS.
                </p>
              </div>
              <div className="mb-4">
                <h3 className="text-lg font-medium text-gray-900 dark:text-gray-100 mb-2">Comments</h3>
                <div className="space-y-4">
                  <div className="flex items-start space-x-4">
                    <img
                      alt="Avatar"
                      className="rounded-full"
                      height={40}
                      src="/placeholder.svg"
                      style={{
                        aspectRatio: "40/40",
                        objectFit: "cover",
                      }}
                      width={40}
                    />
                    <div>
                      <h4 className="font-medium text-gray-900 dark:text-gray-100">Jane Doe</h4>
                      <p className="text-gray-700 dark:text-gray-400">
                        This is a great guide! I learned a lot about Tailwind CSS.
                      </p>
                      <div className="flex items-center space-x-2 mt-2">
                        <Button size="icon" variant="ghost">
                          <ThumbsUpIcon className="h-5 w-5" />
                          <span className="sr-only">Like</span>
                        </Button>
                        <Button size="icon" variant="ghost">
                          <ThumbsDownIcon className="h-5 w-5" />
                          <span className="sr-only">Dislike</span>
                        </Button>
                        <Button size="icon" variant="ghost">
                          <ReplyIcon className="h-5 w-5" />
                          <span className="sr-only">Reply</span>
                        </Button>
                      </div>
                    </div>
                  </div>
                  <div className="flex items-start space-x-4">
                    <img
                      alt="Avatar"
                      className="rounded-full"
                      height={40}
                      src="/placeholder.svg"
                      style={{
                        aspectRatio: "40/40",
                        objectFit: "cover",
                      }}
                      width={40}
                    />
                    <div>
                      <h4 className="font-medium text-gray-900 dark:text-gray-100">Bob Smith</h4>
                      <p className="text-gray-700 dark:text-gray-400">
                        I've been using Tailwind CSS for a while now and I love it! This guide is really helpful.
                      </p>
                      <div className="flex items-center space-x-2 mt-2">
                        <Button size="icon" variant="ghost">
                          <ThumbsUpIcon className="h-5 w-5" />
                          <span className="sr-only">Like</span>
                        </Button>
                        <Button size="icon" variant="ghost">
                          <ThumbsDownIcon className="h-5 w-5" />
                          <span className="sr-only">Dislike</span>
                        </Button>
                        <Button size="icon" variant="ghost">
                          <ReplyIcon className="h-5 w-5" />
                          <span className="sr-only">Reply</span>
                        </Button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div>
                <h3 className="text-lg font-medium text-gray-900 dark:text-gray-100 mb-2">Leave a Comment</h3>
                <div className="flex items-start space-x-4">
                  <img
                    alt="Avatar"
                    className="rounded-full"
                    height={40}
                    src="/placeholder.svg"
                    style={{
                      aspectRatio: "40/40",
                      objectFit: "cover",
                    }}
                    width={40}
                  />
                  <div className="flex-1">
                    <Textarea className="mb-2" placeholder="Write your comment..." />
                    <div className="flex items-center justify-end">
                      <Button>Submit</Button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="fixed bottom-6 right-6" />
      </main>
    </>
  )
}

function BookmarkIcon(props) {
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
      <path d="m19 21-7-4-7 4V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2v16z" />
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
      <rect width="8" height="4" x="8" y="2" rx="1" ry="1" />
      <path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2" />
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
      <path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
      <polyline points="9 22 9 12 15 12 15 22" />
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
      <line x1="4" x2="20" y1="12" y2="12" />
      <line x1="4" x2="20" y1="6" y2="6" />
      <line x1="4" x2="20" y1="18" y2="18" />
    </svg>
  )
}


function ReplyIcon(props) {
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
      <polyline points="9 17 4 12 9 7" />
      <path d="M20 18v-2a4 4 0 0 0-4-4H4" />
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
      <circle cx="6" cy="6" r="3" />
      <path d="M8.12 8.12 12 12" />
      <path d="M20 4 8.12 15.88" />
      <circle cx="6" cy="18" r="3" />
      <path d="M14.8 14.8 20 20" />
    </svg>
  )
}


function ShareIcon(props) {
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
      <path d="M4 12v8a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-8" />
      <polyline points="16 6 12 2 8 6" />
      <line x1="12" x2="12" y1="2" y2="15" />
    </svg>
  )
}


function ThumbsDownIcon(props) {
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
      <path d="M17 14V2" />
      <path d="M9 18.12 10 14H4.17a2 2 0 0 1-1.92-2.56l2.33-8A2 2 0 0 1 6.5 2H20a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2h-2.76a2 2 0 0 0-1.79 1.11L12 22h0a3.13 3.13 0 0 1-3-3.88Z" />
    </svg>
  )
}


function ThumbsUpIcon(props) {
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
      <path d="M7 10v12" />
      <path d="M15 5.88 14 10h5.83a2 2 0 0 1 1.92 2.56l-2.33 8A2 2 0 0 1 17.5 22H4a2 2 0 0 1-2-2v-8a2 2 0 0 1 2-2h2.76a2 2 0 0 0 1.79-1.11L12 2h0a3.13 3.13 0 0 1 3 3.88Z" />
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