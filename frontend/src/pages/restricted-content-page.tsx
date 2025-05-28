import { Button } from "@/components/ui/button";
import { SignInButton } from "@clerk/clerk-react";

const RestrictedContentPage = () => {
  return (
    <div className="flex flex-col gap-2 items-baseline">
      <h2 className="text-3xl font-extrabold">Restricted Content</h2>
      <p>Please sign in to view the desired content.</p>
      <SignInButton>
        <Button variant={"default"} size={"sm"} className="px-2 min-w-[40px]">
          Sign In
        </Button>
      </SignInButton>
    </div>
  );
};

export default RestrictedContentPage;
