// imported from shadcn/ui

import * as React from "react";
import * as PopoverPrimitive from "@radix-ui/react-popover";

import { cn } from "@/lib/utils"; // Assuming cn is available or a simplified version

function Popover({
  ...props
}: React.ComponentProps<typeof PopoverPrimitive.Root>) {
  return <PopoverPrimitive.Root data-slot="popover" {...props} />;
}

function PopoverTrigger({
  ...props
}: React.ComponentProps<typeof PopoverPrimitive.Trigger>) {
  return <PopoverPrimitive.Trigger data-slot="popover-trigger" {...props} />;
}

function PopoverContent({
  className,
  align = "center",
  sideOffset = 4,
  ...props
}: React.ComponentProps<typeof PopoverPrimitive.Content>) {
  return (
    <PopoverPrimitive.Portal>
      <PopoverPrimitive.Content
        data-slot="popover-content"
        align={align}
        sideOffset={sideOffset}
        className={cn(
          // Brutalist changes for PopoverContent:
          // - bg-popover text-popover-foreground using theme variables
          // - Removed rounded-md for sharp corners
          // - Removed shadow-md for a flat appearance
          // - Strong border-2 border-border
          // - Removed transition animations for abrupt changes
          // - Strong focus outline with ring color
          "bg-background text-foreground z-50 w-72 origin-(--radix-popover-content-transform-origin) rounded-none border-2 border-border p-4 outline-none", // Brutalist styling with variables
          "focus-visible:outline-2 focus-visible:outline-offset-0 focus-visible:outline-ring", // Stark focus outline
          "data-[state=open]:animate-none data-[state=closed]:animate-none", // Remove all animations
          className
        )}
        {...props}
      />
    </PopoverPrimitive.Portal>
  );
}

function PopoverAnchor({
  ...props
}: React.ComponentProps<typeof PopoverPrimitive.Anchor>) {
  return <PopoverPrimitive.Anchor data-slot="popover-anchor" {...props} />;
}

export { Popover, PopoverTrigger, PopoverContent, PopoverAnchor };
