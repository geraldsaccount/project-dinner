import { cn } from "@/lib/utils";
import type { MobileNavProps } from "@/types";
import { Separator } from "@radix-ui/react-separator";
import { Link } from "react-router-dom";
import { Button } from "../ui/button";
import { footerItems } from "@/data/navigation-data";
import { SignedOut, SignInButton } from "@clerk/clerk-react";

export function Sidebar({ items, activeItem, setActiveItem }: MobileNavProps) {
  return (
    <div className="flex flex-col justify-between h-full py-4 bg-background border-r-2 border-foreground">
      {" "}
      {/* Brutalist: Solid background, thick foreground right border */}
      <div className="px-7">
        <div className="flex justify-end">
          <Link to="/" className="flex items-center">
            <span className="font-bold text-xl uppercase text-foreground">
              Killinary
            </span>{" "}
            {/* Brutalist: Uppercase, strong foreground color */}
          </Link>
        </div>

        <div className="flex flex-col space-y-3 mt-6">
          {items.map((item) => (
            <Link
              key={item.to}
              to={item.to}
              className={cn(
                "text-base font-bold uppercase transition-all duration-0 hover:bg-foreground hover:text-background text-right px-2 py-1" /* Brutalist: Bold, uppercase, stark hover, no transition duration */,
                item.to === activeItem
                  ? "bg-foreground text-background" // Active item: solid foreground background, background text
                  : "text-foreground"
              )}
              onClick={() => setActiveItem(item.to)}
            >
              {item.title}
            </Link>
          ))}
        </div>
      </div>
      <div className="px-7 mt-auto">
        <Separator className="my-4 bg-foreground h-[2px]" />{" "}
        {/* Brutalist: Thick, solid foreground separator */}
        <footer className="flex flex-col space-y-2 mb-4">
          {footerItems.map((link) => (
            <Link
              key={link.title}
              to={link.to}
              className="text-sm font-bold uppercase text-foreground hover:bg-foreground hover:text-background text-right duration-0 px-2 py-1" /* Brutalist: Bold, uppercase, stark hover, no transition duration */
            >
              {link.title}
            </Link>
          ))}
        </footer>
        <SignedOut>
          <div className="flex gap-1">
            <SignInButton>
              <Button
                variant={"default"}
                size={"sm"}
                className="px-4 py-2 bg-foreground text-background border-2 border-foreground hover:bg-background hover:text-foreground duration-0 rounded-none w-full" /* Brutalist: High contrast, sharp edges, full width, no transition duration */
              >
                Sign In
              </Button>
            </SignInButton>
          </div>
        </SignedOut>
      </div>
    </div>
  );
}
