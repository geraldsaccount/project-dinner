import * as React from "react";
import * as AvatarPrimitive from "@radix-ui/react-avatar";

import { cn } from "@/lib/utils";

function Avatar({
  className,
  ...props
}: React.ComponentProps<typeof AvatarPrimitive.Root>) {
  return (
    <AvatarPrimitive.Root
      data-slot="avatar"
      className={cn(
        // Brutalist changes for Avatar Root:
        // - Removed rounded-full for sharp square
        // - Added strong border (border-2 border-border)
        // - Increased size for more presence
        "relative flex size-12 shrink-0 overflow-hidden rounded-none border-2 border-border", // Brutalist styling
        className
      )}
      {...props}
    />
  );
}

function AvatarImage({
  className,
  ...props
}: React.ComponentProps<typeof AvatarPrimitive.Image>) {
  return (
    <AvatarPrimitive.Image
      data-slot="avatar-image"
      className={cn("aspect-square size-full object-cover", className)} // Added object-cover for better image fitting
      {...props}
    />
  );
}

function AvatarFallback({
  className,
  ...props
}: React.ComponentProps<typeof AvatarPrimitive.Fallback>) {
  return (
    <AvatarPrimitive.Fallback
      data-slot="avatar-fallback"
      className={cn(
        // Brutalist changes for Avatar Fallback:
        // - Removed rounded-full for sharp square
        // - bg-primary with text-primary-foreground for high contrast
        "bg-primary text-primary-foreground flex size-full items-center justify-center rounded-none font-bold uppercase text-lg", // Brutalist styling
        className
      )}
      {...props}
    />
  );
}

export { Avatar, AvatarImage, AvatarFallback };
