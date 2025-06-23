import PageHeader from "@/components/shared/page-header";
import { Button } from "@/components/ui/button";
import { SignInButton } from "@clerk/clerk-react";

const RestrictedContentPage = () => {
  return (
    <div className="flex flex-col gap-2 items-baseline">
      <PageHeader title="Restricted Content" />

      <p>Please sign in to view the desired content.</p>
      <SignInButton>
        <Button>
          Sign In
        </Button>
      </SignInButton>
    </div>
  );
};

export default RestrictedContentPage;
