// imported from shadcn/ui

import * as React from "react";

import { cn } from "@/lib/utils"; // Assuming cn is available or a simplified version

function Card({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="card"
      // Brutalist changes for the card container:
      // - Removed rounded-xl and shadow-sm
      // - Strong border-2 border-border
      // - Changed background to 'background' and text to 'foreground' for main card body
      // - Important: Removed py-6 from the Card itself to allow CardHeader to extend to the top
      className={cn(
        "bg-background text-foreground flex flex-col gap-6 border-2 border-primary", // Brutalist styling
        className
      )}
      {...props}
    />
  );
}

function CardHeader({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="card-header"
      // Brutalist changes for CardHeader:
      // - Set background to primary and text to primary-foreground
      // - Added px-6 py-6 directly to header for internal padding
      // - Kept border-b-2 border-border for separation
      className={cn(
        "@container/card-header grid auto-rows-min grid-rows-[auto_auto] items-start gap-1.5 px-6 py-6 has-data-[slot=card-action]:grid-cols-[1fr_auto] border-b-2 border-border bg-primary text-primary-foreground", // Brutalist styling
        className
      )}
      {...props}
    />
  );
}

function CardTitle({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="card-title"
      // Bold and uppercase for brutalist typography, now ensuring text color from parent header
      className={cn("leading-none font-bold uppercase text-lg", className)}
      {...props}
    />
  );
}

function CardDescription({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="card-description"
      // Keep simple, text color will inherit from CardHeader's primary-foreground
      className={cn("text-sm opacity-80", className)} // Used opacity for subtle difference within header
      {...props}
    />
  );
}

function CardAction({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="card-action"
      className={cn(
        "col-start-2 row-span-2 row-start-1 self-start justify-self-end",
        className
      )}
      {...props}
    />
  );
}

function CardContent({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="card-content"
      // Added px-6 py-6 to CardContent for internal padding
      className={cn("px-6 pb-6", className)}
      {...props}
    />
  );
}

function CardFooter({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="card-footer"
      // Emphasize footer separation with a bold top border, using border variable
      // Added px-6 py-6 to CardFooter for internal padding
      className={cn(
        "flex items-center px-6 py-6 border-t-2 border-border",
        className
      )} // Brutalist styling with variables
      {...props}
    />
  );
}

export {
  Card,
  CardHeader,
  CardFooter,
  CardTitle,
  CardAction,
  CardDescription,
  CardContent,
};
