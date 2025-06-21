// imported from shadcn/ui

import { cn } from "@/lib/utils";

function Skeleton({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="skeleton"
      // Brutalist changes for Skeleton:
      // - Removed rounded-md for sharp corners
      // - Added a subtle border for definition (using 'border' color)
      // - Maintained bg-accent and animate-pulse
      className={cn(
        "bg-accent animate-pulse rounded-none border border-border",
        className
      )} // Brutalist styling
      {...props}
    />
  );
}

export { Skeleton };
