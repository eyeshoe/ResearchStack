package co.touchlab.researchstack.common.task;
import android.content.Context;
import android.content.res.Resources;

import java.util.List;

import co.touchlab.researchstack.R;
import co.touchlab.researchstack.ResearchStackApplication;
import co.touchlab.researchstack.common.model.ConsentDocument;
import co.touchlab.researchstack.common.model.ConsentSection;
import co.touchlab.researchstack.common.model.ConsentSectionModel;
import co.touchlab.researchstack.common.model.ConsentSignature;
import co.touchlab.researchstack.common.result.TaskResult;
import co.touchlab.researchstack.common.step.ConsentReviewStep;
import co.touchlab.researchstack.common.step.ConsentSharingStep;
import co.touchlab.researchstack.common.step.ConsentVisualStep;
import co.touchlab.researchstack.common.step.Step;
import co.touchlab.researchstack.utils.JsonUtils;

public class ConsentTask extends OrderedTask
{

    public ConsentTask(Context context)
    {
        super("consent");

        Resources r = context.getResources();

        //TODO Read on main thread for intense UI blockage.
        int consentResId =  ResearchStackApplication.getInstance().getConsentSections();
        String fileName = r.getResourceEntryName(consentResId);
        ConsentSectionModel data = JsonUtils.loadClassFromRawJson(context, ConsentSectionModel.class,
                                                                fileName);

        List<ConsentSection> sections = data.getSections();
        ConsentSignature signature = new ConsentSignature(r.getString(R.string.participant), null, "participant");
        signature.setRequiresSignatureImage(ResearchStackApplication.getInstance().isSignatureEnabledInConsent());

        ConsentDocument consent = new ConsentDocument();
        consent.setTitle(r.getString(R.string.signature_page_title));
        consent.setSignaturePageTitle(r.getString(R.string.signature_page_title));
        consent.setSignaturePageContent(r.getString(R.string.signature_page_content));
        consent.setSections(sections);
        consent.addSignature(signature);

        //TODO Add Quiz

        //TODO Add Quiz Evaluation

        ConsentVisualStep visualStep = new ConsentVisualStep("visual", consent);
        addStep(visualStep);

        ConsentSharingStep sharingStep = new ConsentSharingStep(r, "sharing", data.getDocumentProperties());
        addStep(sharingStep);

        String reasonForConsent = r.getString(R.string.consent_review_reason);
        ConsentReviewStep reviewStep = new ConsentReviewStep("reviewStep", signature, consent, reasonForConsent);
        addStep(reviewStep);
    }

    @Override
    public Step getStepAfterStep(Step step, TaskResult result)
    {
        return super.getStepAfterStep(step, result);
    }

    @Override
    public Step getStepBeforeStep(Step step, TaskResult result)
    {
        return super.getStepBeforeStep(step, result);
    }

    //  -(ORKStep *)stepBeforeStep:(ORKStep *)__unused step withResult:(ORKTaskResult *)__unused result{
//      return nil;
//  }
//
//  -(ORKStep *)stepAfterStep:(ORKStep *)step withResult:(ORKTaskResult *)__unused result{
//
//      APCUser *user = ((APHAppDelegate *)[UIApplication sharedApplication].delegate ).dataSubstrate.currentUser;
//      ORKStep *nextStep;
//      if (user.isSignedIn) {
//          if (!step) {
//              nextStep = [self.steps objectAtIndex:0];
//          }else{
//              nextStep = nil;
//              [[NSNotificationCenter defaultCenter] postNotificationName:kReturnControlOfTaskDelegate object:nil];
//          }
//      }else if(user.isSignedUp){
//          if (!step) {
//              nextStep = [self.steps objectAtIndex:0];
//          }else{
//              nextStep = nil;
//              [[NSNotificationCenter defaultCenter] postNotificationName:kReturnControlOfTaskDelegate object:nil];
//          }
//      }else{
//
//          if (!step) {
//              nextStep = [self.steps objectAtIndex:0];
//          }else if ([step.identifier isEqualToString:@"consentStep"]) {
//              nextStep = [self.steps objectAtIndex:1];
//          }else if ([step.identifier isEqualToString:@"question1"]) {
//              nextStep = [self.steps objectAtIndex:2];
//          }else if ([step.identifier isEqualToString:@"question2"]) {
//              nextStep = [self.steps objectAtIndex:3];
//          }else if ([step.identifier isEqualToString:@"question3"]) {
//              nextStep = [self.steps objectAtIndex:4];
//          }else if ([step.identifier isEqualToString:@"quizEvaluation"]) {
//
//              if (self.passedQuiz) {
//                  nextStep = [self.steps objectAtIndex:5];//return to consent review step
//              }else if (self.failedAttempts == 1) {
//                  nextStep = [self.steps objectAtIndex:1];//return to quiz
//              }else {
//                  nextStep = [self.steps objectAtIndex:0];//reassurance steps
//              }
//
//          }else if ([step isKindOfClass:[ORKConsentReviewStep class]]) {
//              nextStep = step;
//          }else{
//              nextStep = nil;
//          }
//
//      }
//
//      return nextStep;
//  }

}
